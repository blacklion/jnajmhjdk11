package jna;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.nio.DoubleBuffer;

/**
 * @author Lev Serebryakov
 */
public class JNAAllocated extends FFTBenchmarkParams {
    @State(org.openjdk.jmh.annotations.Scope.Thread)
    public static class BenchState extends FFTState {
        DoubleBuffer i;
        DoubleBuffer o;
        FFTW3Library.fftw_plan p = null;

        @Setup(Level.Trial)
       	public void Setup(BenchmarkParams params) {
            Setup(Integer.parseInt(params.getParam("size")), Boolean.parseBoolean(params.getParam("inPlace")));
       	}

        public void Setup(int size, boolean inPlace) {
       	    super.Setup(size, inPlace);

            i = DoubleBuffer.allocate(size * 2);
            if (inPlace)
                o = i;
            else
                o = DoubleBuffer.allocate(size * 2);

            p = FFTW3Library.INSTANCE.fftw_plan_dft_1d(size, i, o, -1, 0);
       	}

        @TearDown(Level.Trial)
        public void TearDown() {
            if (p != null)
                FFTW3Library.INSTANCE.fftw_destroy_plan(p);
        }
    }


    @Benchmark
    public void FFTOnly(BenchState state) {
        FFTW3Library.INSTANCE.fftw_execute(state.p);
    }

    @Benchmark
    public void Full(BenchState state) {
        // In place or not we need to copy data in and copy data out
        // maybe, between two arrays and not four, but still
        state.i.rewind();
        state.i.put(state.ji);
        FFTW3Library.INSTANCE.fftw_execute(state.p);
        state.o.rewind();
        state.o.get(state.jo);
    }
}
