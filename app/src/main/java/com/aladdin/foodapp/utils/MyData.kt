package com.aladdin.foodapp.utils

import com.aladdin.foodapp.models.Burger
import com.aladdin.foodapp.models.FoodHome
import com.google.firebase.auth.PhoneAuthProvider

object MyData {

    var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    var index = 0


    fun getArraylist(): ArrayList<Burger> {

        val arrayList = ArrayList<Burger>()
        arrayList.add(
            Burger(
                "https://pngimg.com/uploads/burger_sandwich/burger_sandwich_PNG4135.png",
                "Burger",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "20000",
                "3.4",
                0,
                "katta", 1
            )
        )
        arrayList.add(
            Burger(
                "https://www.pngfind.com/pngs/m/74-743438_grill-burger-png-bacon-cheeseburger-png-transparent-png.png",
                "Big Burger",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                0, "katta", 1
            )
        )
        arrayList.add(
            Burger(
                "https://www.citypng.com/public/uploads/preview/meat-burger-fast-food-illustration-png-image-11653074037kwxlikntuv.png",
                "Super Burger",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "24000",
                "3.4",
                0, "katta", 1
            )
        )
        arrayList.add(
            Burger(
                "https://pngimg.com/uploads/burger_sandwich/burger_sandwich_PNG4157.png",
                "Burger",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                0, "katta", 1
            )
        )
        arrayList.add(
            Burger(
                "https://parspng.com/wp-content/uploads/2022/05/pizzapng.parspng.com-8.png",
                "Pitsa Italiano",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                1, "katta", 1
            )
        )
        arrayList.add(
            Burger(
                "https://pngimg.com/uploads/pizza/pizza_PNG43981.png",
                "Pitsa americano",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                1, "katta", 1
            )
        )
        arrayList.add(
            Burger(
                "https://upload.wikimedia.org/wikipedia/commons/3/39/Supreme_pizza.png",
                "Pitsa Uzbekino",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                1, "katta", 1
            )
        )

        arrayList.add(
            Burger(
                "https://pngimg.com/uploads/hot_dog/hot_dog_PNG10217.png",
                "Hot-dog simple",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "10000",
                "3.4",
                2, "katta", 1
            )
        )

        arrayList.add(
            Burger(
                "https://i.pinimg.com/originals/d5/85/2f/d5852fcbfcc5a14c662d11eb1e5da1fe.png",
                "Hot-dog super",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "19000",
                "3.4",
                2, "katta", 1
            )
        )



        arrayList.add(
            Burger(
                "https://www.seekpng.com/png/detail/12-125885_free-png-hot-dog-png-images-transparent-hot.png",
                "Hot-dog super mega",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "21000",
                "3.4",
                2, "katta", 1
            )
        )


        arrayList.add(
            Burger(
                "https://www.freepnglogos.com/uploads/hot-dog-png/hot-dog-icon-noto-emoji-food-drink-iconset-google-38.png",
                "Hot dog simple",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "12000",
                "3.4",
                2, "katta", 1
            )
        )


        arrayList.add(
            Burger(
                "https://www.seekpng.com/png/detail/186-1867076_hot-dog-bun-60-g-chicago-style-hot.png",
                "Hot-dog of students",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "4000",
                "3.4",
                2, "katta", 1
            )
        )
        return arrayList
    }


    public var arrayList = ArrayList<FoodHome>()

    val lll = LastChance()
    val aaa = LastChance2()
    val swipe = LastChance3()


    var categoryList = ArrayList<String>()

}