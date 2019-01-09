package bitstructures;
public interface ImplementedMatrix extends IMatrix{
	/**
	 * set the bit in (i-j)-th position to 1 or 0 according value of <b>value parameter</b>
	 * @param i index of rows
	 * @param j index of columns
	 * @param value if the bit that you want to insert is 1 then it must be <b>true</b>, otherwise it must be <b>false</b>
	 */
	public void set(int i,int j,boolean value);
}
