package drivers;

import java.sql.SQLException;

import basicas.Package;
import basicas.Routine;

public class SqlServerDriver extends DBMSDriver {

	@Override
	public String[] getAllPackagesNames() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRoutineSource(Routine routine) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRoutinesCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Routine[] getRoutinesFromPackage(String packageName)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPackageSource(Package pack) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createPackage(String packDec, String pacBody)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPrimitiveType(String s) {
		// TODO Auto-generated method stub
		return false;
	}

}
