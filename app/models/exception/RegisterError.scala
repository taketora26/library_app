package models.exception

abstract sealed class RegisterError(val message: String)

object DuplicateBookNameError extends RegisterError("すでに同一の名前の本が登録されています。")
