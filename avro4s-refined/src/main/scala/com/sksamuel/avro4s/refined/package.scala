package com.sksamuel.avro4s

import eu.timepit.refined.api.{RefType, Validate}

package object refined {

  implicit def refinedSchemaFor[T: SchemaFor, P, F[_, _]]: SchemaFor[F[T, P]] =
    (namingStrategy: NamingStrategy) => SchemaFor[T].schema(namingStrategy)

  implicit def refinedEncoder[T: Encoder, P, F[_, _] : RefType]: Encoder[F[T, P]] =
    Encoder[T].comap(RefType[F].unwrap)

  implicit def refinedDecoder[T: Decoder, P, F[_, _] : RefType](implicit validate: Validate[T, P]): Decoder[F[T, P]] =
    Decoder[T].map(RefType[F].refine[P].unsafeFrom[T])
}
