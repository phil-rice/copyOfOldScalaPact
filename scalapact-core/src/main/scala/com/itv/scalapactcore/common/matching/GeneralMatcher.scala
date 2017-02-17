package com.itv.scalapactcore.common.matching

trait GeneralMatcher {

  protected def generalMatcher[A, Outcome](expected: Option[A], received: Option[A], success: Outcome, expectedButMissing: Outcome, predicate: (A, A) => Outcome): Outcome =
    (expected, received) match {
      case (None, None) => success

      case (Some(null), Some(null)) => success
      case (None, Some(null)) => success
      case (Some(null), None) => success

      case (Some("null"), Some("null")) => success
      case (None, Some("null")) => success
      case (Some("null"), None) => success

      case (None, Some(_)) => success
      case (Some(_), None) => expectedButMissing
      case (Some(e), Some(r)) => predicate(e, r)
    }

}
