import org.example.GamePos
import org.example.Movement
import org.example.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MainTest {
    @Test
    fun `first test`() {
        val gamePos = GamePos(pieceToMove = true)
        val b = gamePos.solve(1)
        assert(b.isEmpty())
        for (y in 0..3) {
            gamePos.updateValueAt(Movement(Position(2, y + 1), true))
        }
        val b2 = gamePos.solve(5)
        assert(b2.isNotEmpty())
        val b3 = gamePos.solve(1)
        assert(b3.isNotEmpty())
        gamePos.updateValueAt(Movement(Position(2, 5), true))
        assert(gamePos.hasWon(Movement(Position(2, 5), true)))
    }
}