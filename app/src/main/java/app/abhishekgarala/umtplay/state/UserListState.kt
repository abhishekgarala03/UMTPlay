package app.abhishekgarala.umtplay.state

data class UserListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddUserDialog: Boolean = false
)