package com.sabidos.infrastructure.helpers

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.sabidos.infrastructure.logging.Logger

class PhoneNumberHelper {

    private val phoneUtil: PhoneNumberUtil by lazy { PhoneNumberUtil.getInstance() }

    fun getFormattedPhoneNumber(phoneNumber: String): String {
        runCatching {
            val swissNumberProto = phoneUtil.parse(phoneNumber, SUPPORTED_COUNTRY_CODE)
            return phoneUtil.format(
                swissNumberProto,
                PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
            )
        }.getOrElse {
            runCatching {
                if (phoneNumber.length > 2 && (it as NumberParseException).errorType == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                    return "$SUPPORTED_COUNTRY_CODE_VALUE$phoneNumber"
                }
            }.onFailure {
                Logger.withTag(PhoneNumberHelper::class.java.simpleName).withCause(it)
            }
            return phoneNumber
        }
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        runCatching {
            val swissNumberProto = phoneUtil.parse(phoneNumber, SUPPORTED_COUNTRY_CODE)
            return phoneUtil.isValidNumber(swissNumberProto)
        }.getOrElse {
            Logger.withTag(PhoneNumberHelper::class.java.simpleName).withCause(it)
            return false
        }
    }

    companion object {
        private const val SUPPORTED_COUNTRY_CODE = "br"
        private const val SUPPORTED_COUNTRY_CODE_VALUE = "+55"
    }

}