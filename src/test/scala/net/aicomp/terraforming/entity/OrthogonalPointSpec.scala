package net.aicomp.terraforming.entity

import org.specs2.mutable.SpecificationWithJUnit

class OrthogonalPointSpec extends SpecificationWithJUnit {

  "OrthogonalPoint" should {
    "show clicked point" in {
      OrthogonalPoint.isInthePoint(OrthogonalPoint(503, 239), OrthogonalPoint(484, 234)) must_== true
      OrthogonalPoint.isInthePoint(OrthogonalPoint(407, 353), OrthogonalPoint(388, 330)) must_== true
      OrthogonalPoint.isInthePoint(OrthogonalPoint(471, 133), OrthogonalPoint(436, 114)) must_== true
      OrthogonalPoint.isInthePoint(OrthogonalPoint(0, 0), OrthogonalPoint(0, 0)) must_== false
    }
  }
}