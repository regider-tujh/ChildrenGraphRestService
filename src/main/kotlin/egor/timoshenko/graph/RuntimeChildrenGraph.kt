package egor.timoshenko.graph

import egor.timoshenko.dto.ChildTmp
import egor.timoshenko.graph.exception.ChildNotFoundException
import egor.timoshenko.graph.exception.ChildSelfReflectionException
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.read
import kotlin.concurrent.write

class RuntimeChildrenGraph : ChildrenGraph {

    private class Node(val value: ChildTmp) {
        private val relations = HashMap<UUID, Node>()
        private val backRefs = HashMap<UUID, Node>()
        var relationOutCounter = 0
        var relationInCounter = 0
        var relationCommonCounter = 0


        fun delete() {
            backRefs.forEach { (_, it) ->
                run {
                    it.relations.remove(value.id)
                    it.relationOutCounter--
                    if (relations.containsKey(it.value.id)) {
                        it.relationCommonCounter--
                    }
                }
            }
            relations.forEach { (_, it) ->
                run {
                    it.backRefs.remove(value.id)
                    it.relationInCounter--
                }
            }
        }

        fun addRelation(node: Node): Boolean {
            return if (!relations.containsKey(node.value.id)) {
                relations[node.value.id] = node
                relationOutCounter++
                node.backRefs[value.id] = this
                node.relationInCounter++
                if (node.relations.containsKey(value.id)) {
                    node.relationCommonCounter++
                    relationCommonCounter++
                }
                true
            } else {
                false
            }
        }

        fun deleteRelation(node: Node): Boolean {
            return if (relations.containsKey(node.value.id)) {
                relations.remove(node.value.id)
                relationOutCounter--
                node.backRefs.remove(value.id)
                node.relationInCounter--
                if (node.relations.containsKey(value.id)) {
                    node.relationCommonCounter--
                    relationCommonCounter--
                }
                true
            } else {
                false
            }
        }

        fun isUnloved() = relationInCounter == 0

        fun isSad() = relationOutCounter > 0 && relationCommonCounter == 0

    }


    private val lock = java.util.concurrent.locks.ReentrantReadWriteLock()
    private val children: MutableMap<UUID, Node> = HashMap()
    private val nameSet: MutableSet<String> = HashSet()

    override fun addChild(child: ChildTmp): Boolean = lock.write {
        return if (nameSet.contains(child.name) || children.containsKey(child.id)) {
            false
        } else {
            children[child.id] = Node(child)
            nameSet.add(child.name)
            true
        }
    }

    override fun deleteChild(childId: UUID): Boolean = lock.write {
        return if (children.containsKey(childId)) {
            val child = children[childId]!!
            children.remove(childId)
            nameSet.remove(child.value.name)
            child.delete()
            true
        } else {
            false
        }
    }

    override fun addRelation(childFromId: UUID, childToId: UUID): Boolean = lock.write {

        if (childFromId == childToId) {
            throw ChildSelfReflectionException(childFromId)
        }

        if (!children.containsKey(childFromId)) {
            throw ChildNotFoundException(childFromId)
        }

        if (!children.containsKey(childToId)) {
            throw ChildNotFoundException(childToId)
        }

        children[childFromId]!!.addRelation(children[childToId]!!)
    }

    override fun deleteRelation(childFromId: UUID, childToId: UUID): Boolean = lock.write {
        if (!children.containsKey(childFromId)) {
            throw ChildNotFoundException(childFromId)
        }

        if (!children.containsKey(childToId)) {
            throw ChildNotFoundException(childToId)
        }

        children[childFromId]!!.deleteRelation(children[childToId]!!)

    }

    override fun getUnlovedChildren(): List<ChildTmp> = lock.read {
        children.values.filter { it.isUnloved() }.map { it.value }
    }

    override fun getSadChildren(): List<ChildTmp> = lock.read {
        children.values.filter { it.isSad() }.map { it.value }
    }

    override fun getLovedChildren(): List<ChildTmp> = lock.read {
        val max = children.values.map { it.relationInCounter }.max()
        return if (max == null) {
            emptyList()
        } else {
            children.values.filter { it.relationInCounter == max }.map { it.value }
        }
    }
}
