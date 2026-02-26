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
}