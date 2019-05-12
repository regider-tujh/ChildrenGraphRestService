package egor.timoshenko.controller.handler

import egor.timoshenko.dto.ChildV
import egor.timoshenko.dto.CreateChild
import egor.timoshenko.graph.ChildrenGraph
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class ChildrenGraphHandler(private val childrenGraph: ChildrenGraph) {

    fun addChild(createChild: CreateChild): ResponseEntity<ChildV> {
        val child = ChildV(
            id = UUID.randomUUID(),
            name = createChild.name
        )
        val isCreated = childrenGraph.addChild(child)
        return if (isCreated) {
            ResponseEntity.status(HttpStatus.CREATED).body(child)
        } else {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    fun deleteChild(child: UUID): ResponseEntity<Nothing> {
        val isDeleted = childrenGraph.deleteChild(child)
        return if (isDeleted) {
            ResponseEntity.status(HttpStatus.OK).build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    fun addRelation(childFrom: UUID, childTo: UUID): ResponseEntity<Nothing> {
        val isCreated = childrenGraph.addRelation(childFrom, childTo)
        return if (isCreated) {
            ResponseEntity.status(HttpStatus.CREATED).build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    fun deleteRelation(childFrom: UUID, childTo: UUID): ResponseEntity<Nothing> {
        val isDeleted = childrenGraph.deleteRelation(childFrom, childTo)
        return if (isDeleted) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun getUnlovedChildren(): ResponseEntity<List<ChildV>> {
        val unlovedChildren = childrenGraph.getUnlovedChildren()
        return ResponseEntity.ok(unlovedChildren)
    }

    fun getSadChildren(): ResponseEntity<List<ChildV>> {
        val sadChildren = childrenGraph.getSadChildren()
        return ResponseEntity.ok(sadChildren)
    }

    fun getLovedChildren(): ResponseEntity<List<ChildV>> {
        val lovedChildren = childrenGraph.getLovedChildren()
        return ResponseEntity.ok(lovedChildren)
    }
}