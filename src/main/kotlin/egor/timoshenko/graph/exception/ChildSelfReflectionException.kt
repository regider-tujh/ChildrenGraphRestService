package egor.timoshenko.graph.exception

import java.util.*

class ChildSelfReflectionException(id: UUID) :
    ChildrenGraphException("Child tried to love themselves with id : $id")