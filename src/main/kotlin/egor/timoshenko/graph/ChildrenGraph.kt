package egor.timoshenko.graph

import egor.timoshenko.dto.ChildV
import java.util.*

interface ChildrenGraph {
    fun addChild(child: ChildV): Boolean

    fun deleteChild(childId: UUID): Boolean

    fun addRelation(childFromId: UUID, childToId: UUID): Boolean

    fun deleteRelation(childFromId: UUID, childToId: UUID): Boolean

    fun getUnlovedChildren(): List<ChildV>

    fun getSadChildren(): List<ChildV>

    fun getLovedChildren(): List<ChildV>

}