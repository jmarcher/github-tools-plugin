package com.nerdscorner.android.plugin.github.ui.tablemodels

import com.nerdscorner.android.plugin.github.domain.gh.GHPullRequestWrapper
import com.nerdscorner.android.plugin.utils.Strings
import java.io.Serializable
import java.text.SimpleDateFormat

class GHPullRequestTableModel(pullRequests: MutableList<GHPullRequestWrapper>, colNames: Array<String>) : BaseModel<GHPullRequestWrapper>(pullRequests, colNames), Serializable {

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        if (rowIndex < 0 || rowIndex >= items.size) {
            return null
        }
        val pullRequestWrapper = items[rowIndex]
        val pullRequest = pullRequestWrapper.ghPullRequest
        when (columnIndex) {
            COLUMN_TITLE -> return pullRequest.title
            COLUMN_AUTHOR -> {
                return try {
                    pullRequest.user.login
                } catch (e: Exception) {
                    null
                }
            }
            COLUMN_DATE -> try {
                return SimpleDateFormat(Strings.DATE_FORMAT).format(pullRequest.updatedAt)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return null
    }

    companion object {
        private const val COLUMN_TITLE = 0
        private const val COLUMN_AUTHOR = 1
        private const val COLUMN_DATE = 2
    }
}