package com.caltyfarm.caltyfarm.utils

import com.caltyfarm.caltyfarm.data.model.ActionHistory
import java.text.SimpleDateFormat
import java.util.*

object TableUtils {
    private const val tableHtmlPrefix =
        "<style>" +
                "body {height: auto}" +
                "table, th, td {border: 1px solid black;border-collapse: collapse;}" +
                "th, td {padding: 10px; word-wrap: break-word;}" +
                ".date {max-width: 100px}" +
                ".action, .condition .diagnostic .drug {max-width: 200px;}" +
                "table {width:100%;}" +
                "</style>" +
                "<body>" +
                "<table style=\"width:100%\">" +
                "  <tr>" +
                "    <th class=\"date\">Tanggal</th>" +
                "    <th class=\"action\">Tindakan</th>" +
                "    <th class=\"condition\">Kondisi Sapi</th>" +
                "    <th class=\"diagnostic\">Diagnosa</th>" +
                "    <th class=\"drug\">Obat/Vaksin</th>" +
                "  </tr>"
    private const val tableHtmlSuffix = "</table></body>"

    fun listOfActionHistoryToTableHtml(actionHistoryList: List<ActionHistory>): String {
        val dateFormatString = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(dateFormatString, Locale.US)
        val tableHtml = StringBuilder()
        tableHtml.append(tableHtmlPrefix)
        for (actionHistory in actionHistoryList) {
            tableHtml.append("<tr>")
            tableHtml.append("<td class=\"date\">${sdf.format(Date(actionHistory.date * 1000))}</td>")
            tableHtml.append("<td class=\"action\">${actionHistory.action}</td>")
            tableHtml.append("<td class=\"condition\">${actionHistory.condition}</td>")
            tableHtml.append("<td class=\"diagnostic\">${actionHistory.diagnostic}</td>")
            tableHtml.append("<td class=\"drug\">${actionHistory.drugOrVaccine ?: ""}</td>")
            tableHtml.append("\n")
            tableHtml.append("</tr>")
        }
        tableHtml.append(tableHtmlSuffix)
        return tableHtml.toString()
    }
}