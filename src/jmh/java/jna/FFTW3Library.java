package jna;

import com.sun.jna.*;

import java.nio.DoubleBuffer;

/**
 * @author Lev Serebryakov
 */
public interface FFTW3Library extends Library {
    public static final String JNA_LIBRARY_NAME = "libfftw3-3";
   	public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(JNA_LIBRARY_NAME);
   	public static final FFTW3Library INSTANCE = Native.load(JNA_LIBRARY_NAME, FFTW3Library.class);

    /**
     *
      */
    public static class fftw_plan extends PointerType {
   		public fftw_plan(Pointer address) {
   			super(address);
   		}
   		public fftw_plan() {
   			super();
   		}
   }

    public fftw_plan fftw_plan_dft_1d(int n, DoubleBuffer in, DoubleBuffer out, int sign, int flags);
    public void fftw_execute(fftw_plan p);
	public void fftw_execute_dft(fftw_plan p, DoubleBuffer in, DoubleBuffer out);
    public void fftw_destroy_plan(fftw_plan p);
}
