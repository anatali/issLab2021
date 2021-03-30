package it.unibo.interaction

import org.json.JSONObject

interface IssObserver {
    fun handleInfo(info: String)
    fun handleInfo(info: JSONObject)
}