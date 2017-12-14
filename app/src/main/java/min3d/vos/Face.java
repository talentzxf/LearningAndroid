package min3d.vos;

/**
 * Simple VO with three properties representing vertex indicies. 
 * Is not necessary for functioning of engine, just a convenience.
 */
public class Face 
{
	public int a;
	public int b;
	public int c;
	
	public Face(int $a, int $b, int $c)
	{
		a = $a;
		b = $b;
		c = $c;
	}
}
