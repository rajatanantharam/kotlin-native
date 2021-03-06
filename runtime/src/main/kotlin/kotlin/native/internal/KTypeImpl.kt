/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

package kotlin.native.internal

import kotlin.reflect.*

internal class KTypeImpl(
        override val classifier: KClassifier?,
        override val arguments: List<KTypeProjection>,
        override val isMarkedNullable: Boolean
) : KType {
    override fun equals(other: Any?) =
            other is KTypeImpl &&
                    this.classifier == other.classifier &&
                    this.arguments == other.arguments &&
                    this.isMarkedNullable == other.isMarkedNullable

    override fun hashCode(): Int {
        return (classifier?.hashCode() ?: 0) * 31 * 31 + this.arguments.hashCode() * 31 + if (isMarkedNullable) 1 else 0
    }

    override fun toString(): String {
        val classifierString = when (classifier) {
            is KClass<*> -> classifier.qualifiedName ?: classifier.simpleName
            else -> null
        } ?: return "(non-denotable type)"

        return buildString {
            append(classifierString)

            if (arguments.isNotEmpty()) {
                append('<')

                arguments.forEachIndexed { index, argument ->
                    if (index > 0) append(", ")

                    if (argument.variance == null) {
                        append('*')
                    } else {
                        append(when (argument.variance) {
                            KVariance.INVARIANT -> ""
                            KVariance.IN -> "in "
                            KVariance.OUT -> "out "
                        })
                        append(argument.type)
                    }
                }

                append('>')
            }

            if (isMarkedNullable) append('?')
        }
    }
}

internal class KTypeImplForGenerics : KType {
    override val classifier: KClassifier?
        get() = error("Generic types are not yet supported in reflection")

    override val arguments: List<KTypeProjection> get() = emptyList()

    override val isMarkedNullable: Boolean
        get() = error("Generic types are not yet supported in reflection")

    override fun equals(other: Any?) =
            error("Generic types are not yet supported in reflection")

    override fun hashCode(): Int =
            error("Generic types are not yet supported in reflection")
}