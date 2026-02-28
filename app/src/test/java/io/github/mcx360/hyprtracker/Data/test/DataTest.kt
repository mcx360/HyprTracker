package io.github.mcx360.hyprtracker.Data.test

import io.github.mcx360.hyprtracker.data.getHyperTensionStage
import junit.framework.TestCase.assertEquals
import org.junit.Test


class DataTests{

    @Test
    fun test_Hypertension_Normal_Stage_Is_correct(){
        val stage = getHyperTensionStage("79","79")
        assertEquals(stage, "Normal")
    }

    @Test
    fun test_Hypertension_Elevated_Stage_Is_Correct(){
        val stage = getHyperTensionStage("122","79")
        assertEquals(stage, "Elevated")
    }

    @Test
    fun test_Hypertension_Stage_1_is_Correct(){
        val stage = getHyperTensionStage("135", "85")
        assertEquals(stage, "Stage 1")
    }

    @Test
    fun test_Hypertension_Stage_2_is_Correct(){
        val stage = getHyperTensionStage("150", "95")
        assertEquals(stage, "Stage 2")
    }

    @Test
    fun Test_Hypertension_Crisis_Stage_Is_Correct(){
        val stage = getHyperTensionStage("200", "140")
        assertEquals(stage, "Hypertension Crisis")
    }

    @Test
    fun Test_ErrorHandling(){
        val stage = getHyperTensionStage("abc","67")
        assertEquals(stage, "error")
    }

    @Test
    fun test_Hypertension_Boundary_Normal_Elevated(){
        val stage = getHyperTensionStage("120", "79") // upper limit of Normal
        assertEquals(stage, "Elevated") // check if it correctly moves to Elevated
    }

    @Test
    fun test_Hypertension_Boundary_Elevated_Stage1(){
        val stage = getHyperTensionStage("129", "79") // upper limit of Elevated
        assertEquals(stage, "Elevated") // should still be Elevated

        val stage2 = getHyperTensionStage("130", "80") // first Stage 1
        assertEquals(stage2, "Stage 1")
    }

    @Test
    fun test_Hypertension_Boundary_Stage1_Stage2(){
        val stage = getHyperTensionStage("139", "89") // upper limit of Stage 1
        assertEquals(stage, "Stage 1")

        val stage2 = getHyperTensionStage("140", "90") // first Stage 2
        assertEquals(stage2, "Stage 2")
    }

    @Test
    fun test_Hypertension_NegativeValues(){
        val stage = getHyperTensionStage("-120", "80")
        assertEquals(stage, "error")

        val stage2 = getHyperTensionStage("120", "-80")
        assertEquals(stage2, "error")

        val stage3 = getHyperTensionStage("0", "0")
        assertEquals(stage3, "error")
    }

    @Test
    fun test_Hypertension_ExtremelyHighValues(){
        val stage = getHyperTensionStage("1000", "800")
        assertEquals(stage, "Hypertension Crisis")
    }

    @Test
    fun test_Hypertension_InvalidStrings(){
        assertEquals(getHyperTensionStage("", "80"), "error")
        assertEquals(getHyperTensionStage("120", ""), "error")
        assertEquals(getHyperTensionStage("one", "two"), "error")
    }

    @Test
    fun test_Hypertension_DecimalValues(){
        val stage = getHyperTensionStage("120.5", "79.5")
        assertEquals(stage, "Elevated")
    }
}