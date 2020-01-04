import java.io.FileFilter

class InvocationHandler implements FileFilter {

	boolean accept(File file) {
		return true
	}

}