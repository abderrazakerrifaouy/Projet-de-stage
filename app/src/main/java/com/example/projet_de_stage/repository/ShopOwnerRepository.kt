package com.example.projet_de_stage.repository


import com.example.projet_de_stage.data.ShopOwner
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ShopOwnerRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("shop_owners")


//    fun addShopOwner(
//        shopOwner: ShopOwner,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        collection.document(shopOwner.uid).set(shopOwner)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { e -> onFailure(e) }
//    }

    fun getShopOwnerById(
        id: String,
        callback: (ShopOwner?) -> Unit
    ) {
        collection.document(id).get()
            .addOnSuccessListener { doc ->
                // تبعث الكائن المحوّل أو null إذا لم يكن موجوداً
                callback(doc.toObject(ShopOwner::class.java))
            }
            .addOnFailureListener {
                // في حال الخطأ، تبعث null أو تتعامل مع الخطأ هنا
                callback(null)
            }
    }

//    fun deleteShopOwner(
//        id: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        collection.document(id).delete()
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { e -> onFailure(e) }
//    }
}
