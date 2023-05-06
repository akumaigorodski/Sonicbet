package dev.sonicbet

import java.math.{MathContext, RoundingMode as JRoundingMode}
import scala.annotation.targetName
import scala.math.BigDecimal.RoundingMode


extension(bigDec: BigDecimal)
  def oneMinus: BigDecimal = BigDecimal(1) - bigDec
  def hundredth: BigDecimal = bigDec - bigDec % 100
  def floor: BigDecimal = bigDec.setScale(0, RoundingMode.FLOOR)
  def ceil: BigDecimal = bigDec.setScale(0, RoundingMode.CEILING)
  
  @targetName("withinEpsilon")
  def ~=(other: BigDecimal): Boolean = (other - bigDec).abs <= Params.epsilon

object Params:
  val mathContext = new MathContext(100, JRoundingMode.HALF_UP)
  val epsilon: BigDecimal = BigDecimal(0.00001)
  val ZERO: BigDecimal = BigDecimal(0)

