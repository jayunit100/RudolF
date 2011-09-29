package javasrc;
import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Hino matrix builder.
 * @author Jpeerindex
 */
public class HinoMatrix 
{
	static File f=new File("hino.txt");
	public static void main(String[] args) throws Exception
	{
		String[][] entries = new String[11][11];
		Iterator l = FileUtils.lineIterator(f);
		while(l.hasNext())
		{
			String[] e= l.next().toString().split("_");
			System.out.println(StringUtils.join(e,' '));
			int p=Integer.parseInt(StringUtils.substringBefore(e[3],"/"))/10;
			int c=Integer.parseInt(StringUtils.substringBefore(e[5],"/"))/10;
			
			p=p;
			c=c;
			System.out.println(p +" "+c);
			
			if(entries[p][c]==null)
				entries[p][c]=e[7].substring(0,4);
			else
			entries[p][c]+=","+e[7].substring(0,4);
		}
		for(int i =0 ; i < 11; i++)
		{
			for(int j=0 ; j<11 ; j++)
			{
				System.out.print(entries[i][j]+"\t");
			}
			System.out.println();
		}
	}
}

