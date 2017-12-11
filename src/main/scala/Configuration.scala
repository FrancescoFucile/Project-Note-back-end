trait Configuration {
  def env(key: String) = sys.env(key)
  implicit class URL(string : String = "") { def / (string: String) = this + string}
}
