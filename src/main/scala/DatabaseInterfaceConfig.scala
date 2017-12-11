trait DatabaseInterfaceConfig extends Configuration{
  val DBUrl = env("DB_URL")

  //  USERS
  def DBGetAllUsersURL = DBUrl / "users"
  def DBAddUsersURL = DBUrl / "users"
  def DBGetUserURL(UID: String) = DBUrl / "users" / UID
  def DBUpdateUserURL(UID: String) = DBUrl / UID
  def DBGetAllUserNotes(UID: String) = DBUrl / UID / "Notes"

  // NOTES
  def DBGetAllNotesURL = DBUrl / "notes"
  def DBAddNoteURL = DBUrl / "notes"
  def DBGetNoteURL(NID: String) = DBUrl / "notes" / NID
  def DBDeleteNoteURL(NID: String) = DBUrl / "notes" / NID
  def DBUpdateNoteURL(NID: String) = DBUrl / "notes" / NID

  //  MESSAGES TYPES
  def uploadResponse = "upload_response"
}
