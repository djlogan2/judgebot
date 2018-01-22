package chessclub.com.icc.uci;

import static org.junit.Assert.*;

import org.junit.Test;

public final class testUCIInfo {

    @Test
    public final void test1() {
        UCIInfo i = new UCIInfo("info currmove a1a2 currmovenumber 10");
        assertFalse(i.haveDepth());
        assertFalse(i.haveSeldepth());
        assertFalse(i.haveTime());
        assertFalse(i.haveNodes());
        assertFalse(i.havePv());
        assertFalse(i.haveMultipv());
        assertFalse(i.haveScore());
        assertFalse(i.haveCp());
        assertFalse(i.haveMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertTrue(i.haveCurrmove());
        assertEquals("a1a2", i.getCurrmove());
        assertTrue(i.haveCurrmovenumber());
        assertEquals(10, i.getCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertFalse(i.haveNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test2() {
        UCIInfo i = new UCIInfo("info depth 1");
        assertTrue(i.haveDepth());
        assertEquals(1, i.getDepth());
        assertFalse(i.haveSeldepth());
        assertFalse(i.haveTime());
        assertFalse(i.haveNodes());
        assertFalse(i.havePv());
        assertFalse(i.haveMultipv());
        assertFalse(i.haveScore());
        assertFalse(i.haveCp());
        assertFalse(i.haveMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertFalse(i.haveNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test3() {
        UCIInfo i = new UCIInfo("info depth 1 seldepth 22 multipv 333 score cp -105 nodes 285 nps 3701 time 77 pv g2f3 abcd 1234");
        assertTrue(i.haveDepth());
        assertEquals(1, i.getDepth());
        assertTrue(i.haveSeldepth());
        assertEquals(22, i.getSeldepth());
        assertTrue(i.haveTime());
        assertEquals(77, i.getTime());
        assertTrue(i.haveNodes());
        assertEquals(285, i.getNodes());
        assertTrue(i.havePv());
        assertEquals("g2f3 abcd 1234", i.getPv());
        assertTrue(i.haveMultipv());
        assertEquals(333, i.getMultipv());
        assertTrue(i.haveScore());
        assertTrue(i.haveCp());
        assertEquals(-105, i.getCp());
        assertFalse(i.haveMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertTrue(i.haveNps());
        assertEquals(3701, i.getNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test4() {
        UCIInfo i = new UCIInfo("info depth 111 seldepth 222 multipv 333 score cp 585 nodes 30 nps 394 time 76 pv g3g4 a987 g1g1 ");
        assertTrue(i.haveDepth());
        assertEquals(111, i.getDepth());
        assertTrue(i.haveSeldepth());
        assertEquals(222, i.getSeldepth());
        assertTrue(i.haveTime());
        assertEquals(76, i.getTime());
        assertTrue(i.haveNodes());
        assertEquals(30, i.getNodes());
        assertTrue(i.havePv());
        assertEquals("g3g4 a987 g1g1", i.getPv());
        assertTrue(i.haveMultipv());
        assertEquals(333, i.getMultipv());
        assertTrue(i.haveScore());
        assertTrue(i.haveCp());
        assertEquals(585, i.getCp());
        assertFalse(i.haveMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertTrue(i.haveNps());
        assertEquals(394, i.getNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test5() {
        UCIInfo i = new UCIInfo("info depth 1 seldepth 2 multipv 3 score mate -8 nodes 117 nps 1519 time 77 pv f6h6 ");
        assertTrue(i.haveDepth());
        assertEquals(1, i.getDepth());
        assertTrue(i.haveSeldepth());
        assertEquals(2, i.getSeldepth());
        assertTrue(i.haveTime());
        assertEquals(77, i.getTime());
        assertTrue(i.haveNodes());
        assertEquals(117, i.getNodes());
        assertTrue(i.havePv());
        assertEquals("f6h6", i.getPv());
        assertTrue(i.haveMultipv());
        assertEquals(3, i.getMultipv());
        assertTrue(i.haveScore());
        assertFalse(i.haveCp());
        assertTrue(i.haveMate());
        assertEquals(-8, i.getMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertTrue(i.haveNps());
        assertEquals(1519, i.getNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test6() {
        UCIInfo i = new UCIInfo("info depth 10 seldepth 34 multipv 1 score cp -8835 nodes 9664 nps 21765 time 444 pv g6g5 e8f7 g5g4 f7b3 g4h5 b1c2 h5h4 c2c1 h4g4 c1d1 g4h4");
        assertTrue(i.haveDepth());
        assertEquals(10, i.getDepth());
        assertTrue(i.haveSeldepth());
        assertEquals(34, i.getSeldepth());
        assertTrue(i.haveTime());
        assertEquals(444, i.getTime());
        assertTrue(i.haveNodes());
        assertEquals(9664, i.getNodes());
        assertTrue(i.havePv());
        assertEquals("g6g5 e8f7 g5g4 f7b3 g4h5 b1c2 h5h4 c2c1 h4g4 c1d1 g4h4", i.getPv());
        assertTrue(i.haveMultipv());
        assertEquals(1, i.getMultipv());
        assertTrue(i.haveScore());
        assertTrue(i.haveCp());
        assertEquals(-8835, i.getCp());
        assertFalse(i.haveMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertTrue(i.haveNps());
        assertEquals(21765, i.getNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test7() {
        UCIInfo i = new UCIInfo("info depth 10 seldepth 2 multipv 1 score mate 1 nodes 144 nps 1870 time 77 pv f6h6 ");
        assertTrue(i.haveDepth());
        assertEquals(10, i.getDepth());
        assertTrue(i.haveSeldepth());
        assertEquals(2, i.getSeldepth());
        assertTrue(i.haveTime());
        assertEquals(77, i.getTime());
        assertTrue(i.haveNodes());
        assertEquals(144, i.getNodes());
        assertTrue(i.havePv());
        assertEquals("f6h6", i.getPv());
        assertTrue(i.haveMultipv());
        assertEquals(1, i.getMultipv());
        assertTrue(i.haveScore());
        assertFalse(i.haveCp());
        assertTrue(i.haveMate());
        assertEquals(1, i.getMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertTrue(i.haveNps());
        assertEquals(1870, i.getNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test8() {
        UCIInfo i = new UCIInfo("info depth 10 seldepth 24 multipv 1 score cp -456 nodes 84359 nps 195275 time 432 pv g8f8 h6h8 f8e7 h8g7 c7e5 g7f6 e5f6 g5f6 e7d8 a2e6 e8e6 h1h8 e6e8 h8h7 d8c7 h7f7 c7b6");
        assertTrue(i.haveDepth());
        assertEquals(10, i.getDepth());
        assertTrue(i.haveSeldepth());
        assertEquals(24, i.getSeldepth());
        assertTrue(i.haveTime());
        assertEquals(432, i.getTime());
        assertTrue(i.haveNodes());
        assertEquals(84359, i.getNodes());
        assertTrue(i.havePv());
        assertEquals("g8f8 h6h8 f8e7 h8g7 c7e5 g7f6 e5f6 g5f6 e7d8 a2e6 e8e6 h1h8 e6e8 h8h7 d8c7 h7f7 c7b6", i.getPv());
        assertTrue(i.haveMultipv());
        assertEquals(1, i.getMultipv());
        assertTrue(i.haveScore());
        assertTrue(i.haveCp());
        assertEquals(-456, i.getCp());
        assertFalse(i.haveMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertTrue(i.haveNps());
        assertEquals(195275, i.getNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test9() {
        UCIInfo i = new UCIInfo("info depth 100 seldepth 2 multipv 1 score mate 1 nodes 414 nps 5111 time 81 pv f6h6 ");
        assertTrue(i.haveDepth());
        assertEquals(100, i.getDepth());
        assertTrue(i.haveSeldepth());
        assertEquals(2, i.getSeldepth());
        assertTrue(i.haveTime());
        assertEquals(81, i.getTime());
        assertTrue(i.haveNodes());
        assertEquals(414, i.getNodes());
        assertTrue(i.havePv());
        assertEquals("f6h6", i.getPv());
        assertTrue(i.haveMultipv());
        assertEquals(1, i.getMultipv());
        assertTrue(i.haveScore());
        assertFalse(i.haveCp());
        assertTrue(i.haveMate());
        assertEquals(1, i.getMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertTrue(i.haveNps());
        assertEquals(5111, i.getNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }

    @Test
    public final void test10() {
        UCIInfo i = new UCIInfo("info nodes 10154779 nps 2243158 time 4527");
        assertFalse(i.haveDepth());
        assertFalse(i.haveSeldepth());
        assertTrue(i.haveTime());
        assertEquals(4527, i.getTime());
        assertTrue(i.haveNodes());
        assertEquals(10154779, i.getNodes());
        assertFalse(i.havePv());
        assertFalse(i.haveMultipv());
        assertFalse(i.haveScore());
        assertFalse(i.haveCp());
        assertFalse(i.haveMate());
        assertFalse(i.haveLowerbound());
        assertFalse(i.haveUpperbound());
        assertFalse(i.haveCurrmove());
        assertFalse(i.haveCurrmovenumber());
        assertFalse(i.haveHashfull());
        assertTrue(i.haveNps());
        assertEquals(2243158, i.getNps());
        assertFalse(i.haveTbhits());
        assertFalse(i.haveCpuload());
        assertFalse(i.haveString());
        assertFalse(i.haveRefutation());
        assertFalse(i.haveLine());
    }
}
