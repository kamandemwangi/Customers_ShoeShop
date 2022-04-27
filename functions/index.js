const functions = require("firebase-functions");
const admin = require('firebase-admin');
const express = require('express');
const {request, response} = require("express");
const { json } = require("body-parser");
const { Change } = require("firebase-functions");
const app = express();

admin.initializeApp();
 
app.post('/', (async (request, response) =>{

    console.log('------------Received M-pesa webhook------------');

    //  const userId = request.body.userId;

    //  console.log("userId: "+userId);


        const MerchantRequestID = request.body.Body.stkCallback.MerchantRequestID;
        const CheckoutRequestID = request.body.Body.stkCallback.CheckoutRequestID;
        const ResultCode =  request.body.Body.stkCallback.ResultCode;
        const ResultDesc = request.body.Body.stkCallback.ResultDesc;
        const CallbackMetadata = request.body.Body.stkCallback.CallbackMetadata;
   

    const responseBody = {
        MerchantRequestID,
        CheckoutRequestID,
        ResultCode,
        ResultDesc,
        CallbackMetadata,
    };

if (request.body.Body.stkCallback.MerchantRequestID !== null) {

    // note for further development
    // if CallbackMetadata.Item[3].Value(phone number) === undifined use CheckoutRequestID
    // if(CallbackMetadata.Item[4].Value !== undefined){
    //     await admin.firestore().collection("customers_payments").doc(CallbackMetadata.Item[4].Value.toString())
    //     .collection("transactions")
    //     .doc(CheckoutRequestID)
    //     .set(responseBody);  
    // }else{
        await admin.firestore().collection("customers_payments").doc(CallbackMetadata.Item[3].Value.toString())
        .collection("transactions")
        .doc(CheckoutRequestID)
        .set(responseBody);
    //}

}



let responseMessage = {
    "ResponseCode": "00000000",
    "ResponseDesc": "success"
      }
     console.log("Phone: "+CallbackMetadata.Item[3].Value.toString());
      response.json(responseMessage);
      response.end;
      


}));

exports.mPesaCallbackUrl = functions.https.onRequest(app);

exports.emptyShoppingBag = functions.firestore
.document("/customers_payments/{phoneNumber}/transactions/{checkoutRequestId}")
.onWrite(async (Change, context) =>{
    const data = Change.after.data();
    if(data === null){ return null; }

    const phoneNumber = context.params.phoneNumber;
    const phoneSubString = phoneNumber.toString().substring(3);

    console.log("PhoneNumber: "+phoneNumber);
    console.log("CheckoutRequestId: "+context.params.checkoutRequestId);

    const usersCollection = admin.firestore().collection("customers");
    const snapshot = await usersCollection.where('phoneNumber', '==', phoneSubString).get();
    if(snapshot.empty){
        console.log("No matching documents.");
        return;
    }

    //fetch userId
    snapshot.forEach(async doc =>{
        console.log(doc.userId, "=>", doc.data().userId);
        const userId = doc.data().userId;

        //get selected shop id
        const selectedShopIdDocRef = await admin.firestore().collection("utils")
        .doc("selected_shopId").get();

        //get shopping bag content
        const shoppingBagCollRef = admin.firestore().collection("customers").doc(userId)
        .collection("shopping_bag");
        const shoppingBagSnapshot  = await shoppingBagCollRef.get();
        shoppingBagSnapshot.forEach(async documents =>{

        const orderNumber = documents.data().orderNumber;
        const productId = documents.data().productId;
        

        if(selectedShopIdDocRef.exists){

        const shopId = selectedShopIdDocRef.data().shopId;

        
        console.log("shopId: "+shopId);
        const productDocRef = await admin.firestore().collection("shops").doc(shopId).
        collection("products")
        .doc(productId)
        .get();

        const updateProductDocRef = admin.firestore().collection("shops").doc(shopId).
        collection("products")
        .doc(productId);
         

        const shoeSizeQuantityList =  await productDocRef.data().shoeSizeQuantityList;
        const shoeColorQuantityList = await productDocRef.data().shoeColorQuantityList;

        for(let i = 0; i < shoeSizeQuantityList.length; i++){
            const shoeSize = documents.data().shoeSize;
            if(shoeSizeQuantityList[i][shoeSize] !== undefined){

            const shoeSizeQuantity = shoeSizeQuantityList[i][shoeSize];

            const preShoeSizeMap = {[shoeSize]:shoeSizeQuantity.toString()};
            const newShoeSizeQuantity = shoeSizeQuantity - 1;

            const postShoeSizeMap = {[shoeSize]:newShoeSizeQuantity.toString()};
            const fieldValue = admin.firestore.FieldValue; 

           await updateProductDocRef.update({shoeSizeQuantityList: fieldValue.arrayRemove(
               preShoeSizeMap
           )});

           await updateProductDocRef.update({shoeSizeQuantityList: fieldValue.arrayUnion(
            postShoeSizeMap
        )});

        await updateProductDocRef.update({shoeSizesList: fieldValue.arrayRemove(
            shoeSize
        )});

        await updateProductDocRef.update({shoeSizesList: fieldValue.arrayUnion(
            shoeSize
        )});
         }

        }

        for(var j = 0; j <shoeColorQuantityList.length; j++){
            const shoeColor = documents.data().shoeColor;
            const fieldValue = admin.firestore.FieldValue; 
            if(shoeColorQuantityList[j][shoeColor] !== undefined){

            const shoeColorQuantity = shoeColorQuantityList[j][shoeColor];

            const preShoeColorMap = {[shoeColor]:shoeColorQuantity.toString()};

            const newShoeColorQuantity = shoeColorQuantity - 1;
            const postShoeColorMap = {[shoeColor]: newShoeColorQuantity.toString()};

            await updateProductDocRef.update({shoeColorQuantityList: fieldValue.arrayRemove(
                preShoeColorMap
            )});
 
            await updateProductDocRef.update({shoeColorQuantityList: fieldValue.arrayUnion(
             postShoeColorMap
         )});
 
         await updateProductDocRef.update({shoeColorsList: fieldValue.arrayRemove(
             shoeColor
         )});
 
         await updateProductDocRef.update({shoeColorsList: fieldValue.arrayUnion(
             shoeColor
         )});

        }
        }


        console.log("shoeColorQuantityList: "+shoeColorQuantityList);
        console.log("shoSizeQuantityList:"+shoeSizeQuantityList);
        //console.log("shoeSizeQuantityList: "+shoeSizeQuantity);
        //console.log("shoeColorQuantityList: "+shoeColorQuantity);


        
        //add shopping items to open_orders collection
        await admin.firestore().collection("customers").doc(userId)
        .collection("open_orders").doc(orderNumber)
        .set(documents.data());

        //empty shopping bag
        if(documents.exists){
            documents.ref.delete();
        }

        //get totals
        const totalsDoc = admin.firestore().collection("customers").doc(userId)
        .collection("customers_utils").doc("totals");

        //delete totals doc
        if((await totalsDoc.get()).exists){
           await totalsDoc.update({totals: 0});
        }else{
            console.log("Document doesn't exist.");
        }

        ////decrement quantity



        
        

    }
        });

    });
     

});