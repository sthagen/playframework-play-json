/*
 * Copyright (C) from 2022 The Play Framework Contributors <https://github.com/playframework>, 2011-2021 Lightbend Inc. <https://www.lightbend.com>
 */

package play.api.libs.json

import scala.deriving.*

import scala.quoted.*

private[json] trait MacroHelpers { self: OptionSupport =>
  type Q <: Quotes
  protected val quotes: Q

  import quotes.reflect.*

  // format: off
  private given q: Q = quotes

  /* Some(A) for Option[A] else None */
  protected object OptionTypeParameter {

    def unapply(tpr: TypeRepr): Option[TypeRepr] = {
      if (self.isOptionalType(tpr)) {
        // TODO: tpr.typeArgs.headOption

        tpr match {
          case AppliedType(_, args) => args.headOption
          case _ => None
        }
      } else None
    }
  }
}

private[json] trait OptionSupport {
  type Q <: Quotes
  protected val quotes: Q

  import quotes.reflect.*

  // format: off
  private given q: Q = quotes

  protected type Opts <: Json.MacroOptions

  protected final lazy val optionTpe: TypeRepr = TypeRepr.of[Option[_]]

  /* Type of compile-time options; See [[MacroOptions]] */
  protected def optsTpe: Type[Opts]
  protected final def optsTpr: TypeRepr = TypeRepr.of(using optsTpe)

  @inline protected final def hasOption[O: Type]: Boolean =
    optsTpr <:< TypeRepr.of[O]

  @inline protected final def isOptionalType(tpr: TypeRepr): Boolean =
    tpr <:< optionTpe
}
