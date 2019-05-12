package egor.timoshenko.graph.exception

import java.util.*

class ChildNotFoundException(id: UUID) :
    ChildrenGraphException("Child not found with id : $id")