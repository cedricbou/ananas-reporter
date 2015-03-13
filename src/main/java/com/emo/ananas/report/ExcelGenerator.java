package com.emo.ananas.report;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import javax.sql.DataSource;

import net.sf.jxls.report.ReportManagerImpl;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.beanutils.BasicDynaBean;

import com.google.common.base.Joiner;

public class ExcelGenerator {

	private final Map<String, DataSource> ds;
	private final File workspace;
	private final File template;
	
	public ExcelGenerator(final Map<String, DataSource> ds, final File workspace, final File template) {
		this.ds = ds;
		this.workspace = workspace;
		this.template = template;
	}
	
	@SuppressWarnings("unchecked")
	public File generate() throws Exception {
		final File result = new File(workspace, UUID.randomUUID().toString() + ".xls");
		
		@SuppressWarnings("rawtypes")
		Map beans = new HashMap();
		
		for(final String name : ds.keySet()) {
			beans.put(name, new ReportManagerImpl(ds.get(name).getConnection(),
		        	beans ));
		}
		
/*		((ReportManagerImpl)beans.get("_demo")).exec("select id, NUM_COL, str_col from CARS").forEach(new Consumer<BasicDynaBean>() {
			@Override
			public void accept(BasicDynaBean t) {
				System.out.println(Joiner.on(',').join(t.getMap().keySet()));
				
			}
		});
*/
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(template.getCanonicalPath(), beans, result.getCanonicalPath());
	
        return result;
	}
}
