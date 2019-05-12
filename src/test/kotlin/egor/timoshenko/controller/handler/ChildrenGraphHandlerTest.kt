package egor.timoshenko.controller.handler

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import egor.timoshenko.dto.ChildV
import egor.timoshenko.dto.CreateChild
import egor.timoshenko.graph.ChildrenGraph
import org.junit.Test
import org.springframework.http.HttpStatus
import java.util.*
import kotlin.test.assertEquals


internal class ChildrenGraphHandlerTest {

    private val childrenGraph = mock<ChildrenGraph>()
    private val pureChild = ChildV(UUID.randomUUID(), "42")
    private val poorChild = ChildV(UUID.randomUUID(), "24")

    @Test
    fun `should return created response`() {
        whenever(childrenGraph.addChild(any())).thenReturn(true)
        val handler = ChildrenGraphHandler(childrenGraph)
        val name = "name"
        val resp = CreateChild(name)
        val child = handler.addChild(resp)
        val body = child.body!!
        assertEquals(name, body.name)
        assertEquals(HttpStatus.CREATED, child.statusCode)
        verify(childrenGraph).addChild(body)
    }

    @Test
    fun `should return conflict response`() {
        whenever(childrenGraph.addChild(any())).thenReturn(false)
        val handler = ChildrenGraphHandler(childrenGraph)
        val name = "name"
        val resp = CreateChild(name)
        val child = handler.addChild(resp)
        assertEquals(HttpStatus.CONFLICT, child.statusCode)
        verify(childrenGraph).addChild(any())
    }

    @Test
    fun `should return sad`() {
        val list = listOf(pureChild)
        whenever(childrenGraph.getSadChildren()).thenReturn(list)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.getSadChildren()
        assertEquals(HttpStatus.OK, ans.statusCode)
        assertEquals(list, ans.body)
        verify(childrenGraph).getSadChildren()
    }


    @Test
    fun `should return  loved`() {
        val list = listOf(pureChild)
        whenever(childrenGraph.getLovedChildren()).thenReturn(list)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.getLovedChildren()
        assertEquals(HttpStatus.OK, ans.statusCode)
        assertEquals(list, ans.body)
        verify(childrenGraph).getLovedChildren()
    }

    @Test
    fun `should return unloved`() {
        val list = listOf(pureChild)
        whenever(childrenGraph.getUnlovedChildren()).thenReturn(list)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.getUnlovedChildren()
        assertEquals(HttpStatus.OK, ans.statusCode)
        assertEquals(list, ans.body)
        verify(childrenGraph).getUnlovedChildren()
    }

    @Test
    fun `should delete child`() {
        whenever(childrenGraph.deleteChild(any())).thenReturn(true)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.deleteChild(pureChild.id)
        assertEquals(HttpStatus.OK, ans.statusCode)
        verify(childrenGraph).deleteChild(pureChild.id)
    }

    @Test
    fun `shouldn't delete child`() {
        whenever(childrenGraph.deleteChild(any())).thenReturn(false)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.deleteChild(pureChild.id)
        assertEquals(HttpStatus.NOT_FOUND, ans.statusCode)
        verify(childrenGraph).deleteChild(pureChild.id)
    }

    @Test
    fun `should create relation`() {
        whenever(childrenGraph.addRelation(any(), any())).thenReturn(true)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.addRelation(
            poorChild.id,
            pureChild.id
        )
        assertEquals(HttpStatus.CREATED, ans.statusCode)
        verify(childrenGraph).addRelation(
            poorChild.id,
            pureChild.id
        )
    }

    @Test
    fun `shouldn't create relation`() {
        whenever(childrenGraph.addRelation(any(), any())).thenReturn(false)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.addRelation(
            poorChild.id,
            pureChild.id
        )
        assertEquals(HttpStatus.NOT_FOUND, ans.statusCode)
        verify(childrenGraph).addRelation(
            poorChild.id,
            pureChild.id
        )
    }

    @Test
    fun `should delete relation`() {
        whenever(childrenGraph.deleteRelation(any(), any())).thenReturn(true)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.deleteRelation(
            poorChild.id,
            pureChild.id
        )
        assertEquals(HttpStatus.OK, ans.statusCode)
        verify(childrenGraph).deleteRelation(
            poorChild.id,
            pureChild.id
        )
    }

    @Test
    fun `shouldn't delete relation`() {
        whenever(childrenGraph.deleteRelation(any(), any())).thenReturn(false)
        val handler = ChildrenGraphHandler(childrenGraph)
        val ans = handler.deleteRelation(
            poorChild.id,
            pureChild.id
        )
        assertEquals(HttpStatus.NOT_FOUND, ans.statusCode)
        verify(childrenGraph).deleteRelation(
            poorChild.id,
            pureChild.id
        )
    }

}