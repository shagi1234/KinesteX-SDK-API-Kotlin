package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.kinestex.kinestexsdkkotlin.secure_api.models.DayInfo
import com.kinestex.kinestexsdkkotlin.secure_api.models.En
import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.WeekInfo

class ConvertDocumentToPlan {
    fun toPlan(document: DocumentSnapshot): Plan {
        return Plan(
            id = document.id,
            imgURL = document.getString("img_URL") ?: "",
            en = extractLanguageInfo(document),
            description = document.getString("description") ?: "",
            categories = extractCategories(document),
            weeks = extractWeeks(document)
        )
    }

    private fun extractLanguageInfo(document: DocumentSnapshot): En {
        val enMap = document.get("en") as? Map<String, Any> ?: mapOf()
        return En(
            title = enMap["title"] as? String ?: "",
            body_parts = (enMap["body_parts"] as? List<String>) ?: listOf(),
            description = enMap["description"] as? String ?: ""
        )
    }

    private fun extractCategories(document: DocumentSnapshot): Map<String, Int> {
        return (document.get("categories") as? Map<String, Long>)?.mapValues { it.value.toInt() } ?: mapOf()
    }

    private fun extractWeeks(document: DocumentSnapshot): List<WeekInfo> {
        val weeksList = document.get("weeks") as? List<Map<String, Any>> ?: listOf()
        return weeksList.map { weekMap ->
            WeekInfo(
                title = weekMap["title"] as? String ?: "",
                description = weekMap["description"] as? String ?: "",
                days = extractDays(weekMap["days"] as? List<Map<String, Any>> ?: listOf())
            )
        }
    }

    private fun extractDays(daysList: List<Map<String, Any>>): List<DayInfo> {
        return daysList.map { dayMap ->
            DayInfo(
                title = dayMap["title"] as? String ?: "",
                description = dayMap["description"] as? String ?: "",
                workout = dayMap["workout"] as? String ?: ""
            )
        }
    }
}