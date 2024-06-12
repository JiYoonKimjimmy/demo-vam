package com.konai.vam.core.common.restclient

enum class ComponentName(private val propertyName: String) {

    FEP("fep"),
    KOD_ITN("koditn"),
    CS("cs"),
    CARD_SE("cardse"),
    CP("cp"),
    ;

    fun getPropertyName(): String = this.propertyName

}