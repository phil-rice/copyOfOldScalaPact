package com.itv.scalapactcore.common.matching

object StatusMatching extends GeneralMatcher {

  lazy val matchStatusCodes: Option[Int] => Option[Int] => MatchStatusOutcome = expected => received =>
    generalMatcher[Int, MatchStatusOutcome](
      expected,
      received,
      MatchStatusSuccess,
      MatchStatusFailed(expected, received),
      (e: Int, r: Int) =>
        if(e == r) MatchStatusSuccess
        else MatchStatusFailed(expected, received)
    )

}
