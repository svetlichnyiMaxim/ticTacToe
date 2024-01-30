package org.example

import kotlin.math.max
import kotlin.math.min

const val SIZE: Int = 7

fun main() {
    val pos = mutableListOf(
        arrayOf<Boolean?>(null, null, null, null, null, null, null),
        arrayOf<Boolean?>(null, null, null, null, null, null, null),
        arrayOf<Boolean?>(null, null, true, null, null, null, null),
        arrayOf<Boolean?>(null, null, true, null, null, null, null),
        arrayOf<Boolean?>(null, null, true, null, null, null, null),
        arrayOf<Boolean?>(null, null, null, null, null, null, null),
        arrayOf<Boolean?>(null, null, null, null, null, null, null),
    )
    val gamePos = GamePos(pos, pieceToMove = true)
    gamePos.display()
    gamePos.solve(4).apply { this.ifEmpty { println("not solved") } }.forEach {
        it.first.display()
    }
}

class GamePos(
    private val pos: MutableList<Array<Boolean?>> = MutableList(SIZE) { Array(SIZE) { null } }, var pieceToMove: Boolean
) {
    private fun copy(): GamePos {
        return GamePos(pos.map { it.toMutableList().toTypedArray() }.toMutableList(), pieceToMove)
    }

    fun updateValueAt(movement: Movement) {
        movement.pos.let {
            pos[it.y][it.x] = movement.newValue
        }
    }

    fun solve(depthLeft: Int): MutableCollection<Pair<GamePos, Movement?>> {
        if (depthLeft == 0) {
            return mutableListOf()
        }
        val result: MutableCollection<MutableCollection<Pair<GamePos, Movement?>>> = mutableListOf()
        possibleMoves().forEach {
            val position = this.produceGamePosition(it)
            if (position.hasWon(it)) {
                return mutableListOf(Pair(position, null))
            }
            val solveResult = position.solve(depthLeft - 1)
            if (solveResult.isNotEmpty()) {
                result.add(solveResult)
            }
        }
        return result.filter { it.last().first.pieceToMove == pieceToMove }.maxByOrNull { it.size } ?: mutableListOf()
    }

    fun hasWon(lastMove: Movement): Boolean {
        val startY = max(lastMove.pos.y - 4, 0)
        val endY = min(lastMove.pos.y + 4, SIZE - 1)
        val startX = max(lastMove.pos.x - 4, 0)
        val endX = min(lastMove.pos.x + 4, SIZE - 1)
        var count = 0
        (startY..endY).forEach { y ->
            if (pos[y][lastMove.pos.x] == lastMove.newValue) {
                if (++count == 5) return true
            } else {
                count = 0
            }
        }
        count = 0
        (startX..endX).forEach { x ->
            if (pos[lastMove.pos.y][x] == lastMove.newValue) {
                if (++count == 5) return true
            } else {
                count = 0
            }
        }
        return false
    }

    private fun possibleMoves(): MutableCollection<Movement> {
        val listOfMoves: MutableCollection<Movement> = mutableListOf()
        for (y in 0..<SIZE) {
            for (x in 0..<SIZE) {
                if (pos[y][x] == null)
                    listOfMoves.add(Movement(Position(y, x), pieceToMove))
            }
        }
        return listOfMoves
    }

    private fun produceGamePosition(move: Movement): GamePos {
        val result = this.copy().apply { updateValueAt(move) }.apply { this.pieceToMove = !this.pieceToMove }
        return result
    }

    fun display() {
        pos.forEach { column ->
            column.forEach { element ->
                print(
                    when (element) {
                        null -> {
                            "- "
                        }

                        true -> {
                            "o "
                        }

                        false -> {
                            "x "
                        }
                    }
                )
            }
            println()
        }
        println()
        println()
    }
}

class Position(val x: Int, val y: Int)

class Movement(val pos: Position, val newValue: Boolean)