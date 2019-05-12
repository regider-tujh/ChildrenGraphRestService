package egor.timoshenko.controller

import egor.timoshenko.controller.handler.ChildrenGraphHandler
import egor.timoshenko.dto.ChildV
import egor.timoshenko.dto.CreateChild
import egor.timoshenko.dto.CreateRelation
import egor.timoshenko.graph.exception.ChildNotFoundException
import egor.timoshenko.graph.exception.ChildSelfReflectionException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/children", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class ChildController(private val childrenGraph: ChildrenGraphHandler) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun addChild(@RequestBody createChild: CreateChild): ResponseEntity<ChildV> {
        return childrenGraph.addChild(createChild)
    }


    @DeleteMapping("/{childId}", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun deleteChild(@PathVariable childId: UUID): ResponseEntity<Nothing> {
        return childrenGraph.deleteChild(childId)
    }

    @PostMapping("/{childFromId}/relations", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun addRelation(@PathVariable childFromId: UUID, @RequestBody childToId: CreateRelation): ResponseEntity<Nothing> {
        return childrenGraph.addRelation(childFromId, childToId.id)
    }


    @DeleteMapping("/{childFromId}/relations/{childToId}", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun deleteRelation(@PathVariable childFromId: UUID, @PathVariable childToId: UUID): ResponseEntity<Nothing> {
        return childrenGraph.deleteRelation(childFromId, childToId)
    }

    @GetMapping("/unloved")
    fun getUnlovedChildren(): ResponseEntity<List<ChildV>> {
        return childrenGraph.getUnlovedChildren()

    }

    @GetMapping("/sad")
    fun getSadChildren(): ResponseEntity<List<ChildV>> {
        return childrenGraph.getSadChildren()
    }

    @GetMapping("/loved")
    fun getLovedChildren(): ResponseEntity<List<ChildV>> {
        return childrenGraph.getLovedChildren()
    }

    @ExceptionHandler(ChildNotFoundException::class)
    fun childNotFound(e: ChildNotFoundException): ResponseEntity<egor.timoshenko.dto.Error> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            egor.timoshenko.dto.Error("404", e.message!!)
        )
    }

    @ExceptionHandler(ChildSelfReflectionException::class)
    fun selReflection(e: ChildSelfReflectionException): ResponseEntity<egor.timoshenko.dto.Error> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            egor.timoshenko.dto.Error("409", e.message!!)
        )
    }


}
