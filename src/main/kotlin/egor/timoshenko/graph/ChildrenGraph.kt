package egor.timoshenko.graph

import egor.timoshenko.dto.ChildTmp
import java.util.*

interface ChildrenGraph {
    fun addChild(child: ChildTmp): Boolean

    fun deleteChild(childId: UUID): Boolean

    fun addRelation(childFromId: UUID, childToId: UUID): Boolean

    fun deleteRelation(childFromId: UUID, childToId: UUID): Boolean

    fun getUnlovedChildren(): List<ChildTmp>

    fun getSadChildren(): List<ChildTmp>

    fun getLovedChildren(): List<ChildTmp>

}