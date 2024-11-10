import com.google.gson.annotations.SerializedName

data class ReminderRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("time")
    val time: Long,
    @SerializedName("remindAt")
    val remindAt: Long
)