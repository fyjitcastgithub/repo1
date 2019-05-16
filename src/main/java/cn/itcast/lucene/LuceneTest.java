package cn.itcast.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneTest {

	/**
	 * 创建索引 将数据写入到索引库 同时建立索引 ==》索引写入 索引写入器 indexWrite
	 * 
	 * @throws Exception
	 */
	@Test
	public void createIndexOne() throws Exception {
	    //创建document对象。
		Document doc = new Document();
		//创建field对象，将field添加到document对象中。
		LongField id = new LongField("id", 1L, Store.YES);
		doc.add(id);
		StringField title = new StringField("title", "谷歌地图之父跳槽FaceBook", Store.YES);
		doc.add(title);
		//指定索引库的存放位置Directory对象
		// 索引库的位置 E:\\Java\\Test_code FSDirectory：文件系统库的位置
		Directory dir = FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"));
		//指定一个分析器，对文档内容进行分析。
		//使用indexwriter对象将document对象写入索引库，此过程进行索引创建。
		// Version.LATEST
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, new StandardAnalyzer());
		// OpenMode打开模式，枚举类，CREATE：覆盖，APPEND：追加，默认为 APPEND
		iwc.setOpenMode(OpenMode.CREATE);
		//创建一个indexwriter对象。
		IndexWriter indexWriter = new IndexWriter(dir, iwc);
		
		indexWriter.addDocument(doc);
		// 提交
		indexWriter.commit();
		//关闭IndexWriter对象。
		indexWriter.close();
	}

	// 创建单个的索引
	@Test
	public void createIndexAPITwo() throws Exception {
		// 准备数据，数据封装到Document对象中
		Document doc = new Document();
		// 数据有不同的字段，字段也有不同的类型
		LongField id = new LongField("id", 12, Store.YES);
		doc.add(id);
		// StringField 可以作为索引，但是不分词，数据会被当成一个词条
		StringField title= new StringField("title","威哥你变了",Store.YES);
		doc.add(title);
		//TextField 可以作为索引，根据索引写入器中的分词器来进行分词，数据被当做多个词条
		TextField content = new TextField("content", "我是北京传智播客", Store.NO);
		doc.add(content);
		Directory dir = FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"));
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, new StandardAnalyzer());
		IndexWriter indexWriter = new IndexWriter(dir, iwc);
		indexWriter.addDocument(doc);
		//提交
		indexWriter.commit();
		indexWriter.close();
	}

	// 批量创建索引
	@Test
	public void createIndexAPIThree() throws Exception {
		    // 创建文档对象集合
			List<Document> docs = new ArrayList<>();
			// 创建文档对象
			Document document1 = new Document();
			document1.add(new StringField("id", "1", Store.YES));
			document1.add(new TextField("title", "谷歌地图之父跳槽FaceBook", Store.YES));
			docs.add(document1);
			// 创建文档对象
			Document document2 = new Document();
			document2.add(new StringField("id", "2", Store.YES));
			document2.add(new TextField("title", "谷歌地图之父加盟FaceBook", Store.YES));
			docs.add(document2);
			// 创建文档对象
			Document document3 = new Document();
			document3.add(new StringField("id", "3", Store.YES));
			document3.add(new TextField("title", "谷歌地图创始人拉斯离开谷歌加盟Facebook", Store.YES));
			docs.add(document3);
			// 创建文档对象
			Document document4 = new Document();
			document4.add(new StringField("id", "4", Store.YES));
			document4.add(new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关", Store.YES));
			docs.add(document4);
			// 创建文档对象
			Document document5 = new Document();
			document5.add(new StringField("id", "5", Store.YES));
			document5.add(new TextField("title", "谷歌地图之父拉斯加盟社交网站Facebook", Store.YES));
			docs.add(document5);
			
			// 索引库对象
			Directory directory = FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"));
			// 创建索引写入器配置对象，1-版本，2-分词器：标准分词器
			IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new StandardAnalyzer());
			// OpenMode打开模式，枚举类，CREATE：覆盖，APPEND：追加，
			conf.setOpenMode(OpenMode.CREATE);
			// 创建索引写入器对象
			IndexWriter indexWriter = new IndexWriter(directory, conf);
			// 执行写入操作
			indexWriter.addDocuments(docs);
			// 提交
			indexWriter.commit();
			// 关闭
			indexWriter.close();
	}

	// IK分词器的使用
	@Test
	public void createIndexAPIFour() throws Exception {
		// 准备数据，数据封装到Document对象中
		Document doc = new Document();
		// 数据有不同的字段，字段也有不同的类型
		LongField id = new LongField("id", 4, Store.YES);
		doc.add(id);
		TextField content = new TextField("content", "未来的IT精英出自黑马", Store.NO);
		doc.add(content);
		Directory dir = FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"));
		// IK分词器的使用
		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(dir, iwc);
		// indexWriter.addDocument(doc);
		List<Document> docs = new ArrayList<Document>();
		docs.add(doc);
		indexWriter.addDocuments(docs);
		// 提交
		indexWriter.commit();
		indexWriter.close();
	}

	// 自定义词库
	@Test
	public void createIndexAPIFive() throws Exception {
		// 准备数据，数据封装到Document对象中
		Document doc = new Document();
		// 数据有不同的字段，字段也有不同的类型
		LongField id = new LongField("id", 5, Store.YES);
		doc.add(id);
		TextField content = new TextField("content", "欢迎威哥来到传智播客学Java", Store.YES);
		TextField title = new TextField("title", "传智播客Itcast,打造中国IT精英人才", Store.YES);
		doc.add(content);
		doc.add(title);
		Directory dir = FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"));
		// IK分词器的使用
		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(dir, iwc);
		// indexWriter.addDocument(doc);
		List<Document> docs = new ArrayList<Document>();
		docs.add(doc);
		indexWriter.addDocuments(docs);
		// 提交
		indexWriter.commit();
		indexWriter.close();
	}

	// 创建索引查询对象
	@Test
	public void searchIndexOne() throws Exception {
		//查询解析器对象
		QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
		Query query = queryParser.parse("传智播客");
		// 创建索引查询对象
		IndexSearcher indexSearcher = new IndexSearcher(
				DirectoryReader.open(FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"))));
		// 排名前n的结果集
		TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
		// 得分文档集合
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc sd : scoreDocs) {
			//倒排列表的id
			int docId = sd.doc;
			//文档的对象
			Document document = indexSearcher.doc(docId);
			System.out.println("搜索到的结果集id：" + document.get("id"));
			System.out.println("搜索到的结果集content：" + document.get("content"));
		}
		// 关闭流 
		indexSearcher.getIndexReader().close();

	}
	
	//查询索引数据   [跟文档匹配]    【查询解析器】
	@Test
	public void testSearcher() throws Exception{
		// 初始化索引库对象
		Directory directory = FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"));
		// 索引读取工具
		IndexReader indexReader = DirectoryReader.open(directory);
		// 索引搜索对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// 创建查询解析器对象   1 参数：查询的目标字段    2 分词器
		QueryParser parser = new QueryParser("content", new IKAnalyzer());
		// 创建查询对象
		Query query = parser.parse("威哥");
		// 执行搜索操作，返回值topDocs包含命中数，得分文档
		TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
		// 打印命中数
		System.out.println("一共命中："+topDocs.totalHits+"条数据");
		// 获得得分文档数组对象，得分文档对象包含得分和文档编号
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			System.out.println("得分："+scoreDoc.score);
			// 文档的编号
			int doc = scoreDoc.doc;
			System.out.println("编号："+doc);
			// 获取文档对象，通过索引读取工具
			Document document = indexReader.document(doc);
			System.out.println("id:"+document.get("id"));
			System.out.println("content:"+document.get("content"));
		}
	}
	

	// 创建索引查询对象   【多字段的查询解析器】   逗比
	@Test
	public void searchIndexTwo() throws Exception {
		// 查询解析器对象
		// QueryParser queryParser=new QueryParser("content", new IKAnalyzer());
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] { "id", "content" },
				new IKAnalyzer());
		//注意此处的参数可以参考   { "id", "content" } 的格式输入
		Query query = queryParser.parse("5,传智播客");

		// 创建索引查询对象
		IndexSearcher indexSearcher = new IndexSearcher(
				DirectoryReader.open(FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"))));
		// 排名前n的结果集
		TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
		// 得分文档集合
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc sd : scoreDocs) {
			int docId = sd.doc;
			Document document = indexSearcher.doc(docId);
			// 文档的得分，即是文档的匹配度 得分越高排名就越靠前
			System.out.println(sd.score);
			System.out.println("搜索到的结果集id：" + document.get("id"));
			System.out.println("搜索到的结果集content：" + document.get("content"));
		}
		// 关闭流
		indexSearcher.getIndexReader().close();
	}

	/**
	 * 抽取一个通用的查询方法
	 */
	public void baseIndex(Query query) throws Exception {
		// 创建索引查询对象
		IndexSearcher indexSearcher = new IndexSearcher(
				DirectoryReader.open(FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"))));
		// 排名前n的结果集
		TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
		// 得分文档集合
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc sd : scoreDocs) {
			int docId = sd.doc;
			Document document = indexSearcher.doc(docId);
			// 文档的得分，即是文档的匹配度 得分越高排名就越靠前
			System.out.println(sd.score);
			System.out.println("搜索到的结果集id：" + document.get("id"));
			System.out.println("搜索到的结果集title：" + document.get("title"));
			System.out.println("搜索到的结果集content：" + document.get("content"));
		}
		// 关闭流
		indexSearcher.getIndexReader().close();
	}

	/**
	 * 特殊查询
	 *  词条搜索 词条来自分词 词条就是索引 是最小的搜索单位 词条可以是一个字，
	 *  一个词或者一句话，甚至一段话
	 * 
	 * @throws Exception
	 * 
	 * 
	 */

	@Test
	public void termQuery() throws Exception {
		// Term(查询的目标字段, 查询的参数);
		//注意此处的 查询的 词条 必须是【 威哥】词条才能匹配上，不能多也不能少
		Term term = new Term("content", "传智播客");  
		Query query = new TermQuery(term);
		//调用通用的查询方法
		baseIndex(query);
	}

	/**
	 * 特殊查询 【通配符查询】
	 * 
	 * @throws Exception
	 * 
	 *  ?只能匹配一个字符
     *  *可以匹配0个或者多个字符
	 */
	@Test
	public void wildcardQuery() throws Exception {
		Term term = new Term("content", "传智?客");
		//Term term = new Term("content", "威哥*");
		Query query = new WildcardQuery(term);
		baseIndex(query);
	}

	/**
	 * 特殊查询 【模糊查询】
	 * 
	 * @throws Exception
	 */
	@Test
	public void fuzzyQuery() throws Exception {
		// Term term=new Term("content", "传智播课");
		Term term = new Term("title", "itcas");
		//容错能力  
		// maxEdits 最大编辑次数，也就是最多错几次 它的值必须在 0-2之间的正整数
		// Query query =new FuzzyQuery(term);
		Query query = new FuzzyQuery(term, 1);
		baseIndex(query);
	}

	/**
	 * 特殊查询 
	 * 数值范围查询 NumericRangeQuery.newLongRange("查询的目标字段", "该字段的最小值",
	 * "该字段的最大值", "是否包含最小值", "是否包含最大值");
	 * 
	 * [6，9]
	 * 
	 * @throws Exception
	 */
	@Test
	public void numberRangeQuery() throws Exception {
		Query query = NumericRangeQuery.newLongRange("id", 5L, 20L, true, true);
		baseIndex(query);
	}

	/**
	 * 特殊查询 组合查询 
	 *   MUST+MUST= 查询的是结果集的交集（返回多个结果集的交集部分）
	 *   SHOULD+SHOULD=查询结果集的并集部分
	 * // 非：Occur.MUST_NOT
	 * 注意顺序 ：先是过滤大的范围的值， 再过滤小范围的值
	 * @throws Exception
	 */
	@Test
	public void booleanQuery() throws Exception {
		BooleanQuery query = new BooleanQuery();
		// 词条搜索：有3条记录
		Term term = new Term("content", "传智播客");
		TermQuery termQuery = new TermQuery(term);
		query.add(termQuery, Occur.MUST);

		NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("id", 4L, 7L, true, true);
		query.add(numericRangeQuery, Occur.SHOULD);
		baseIndex(query);
	}

	/**
	 * 修改索引
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void updateIndex() throws Exception {
		IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
		IndexWriter IndexWriter = new IndexWriter(FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory")), conf);
		Term term = new Term("content", "传智播客");
		Document doc = new Document();
		LongField id = new LongField("id", 30L, Store.YES);
		doc.add(id);
		TextField title = new TextField("title", "谷歌地图之父跳槽到FaceBook", Store.YES);
		doc.add(title);
		TextField content = new TextField("content", "黑马程序员是未来社会的栋梁", Store.YES);
		doc.add(content);
		//根据指定的词条进行搜索，所有与词条匹配的内容将会被指定的 doc覆盖
		IndexWriter.updateDocument(term, doc);
		IndexWriter.commit();
		IndexWriter.close();
	}
	
	/**
	 * 删除索引
	 * @throws Exception
	 */
	@Test
	public void deleteIndex() throws Exception {
		IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
		IndexWriter IndexWriter = new IndexWriter(FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory")), conf);
		//根据词条进行删除
		//IndexWriter.deleteDocuments(new Term("title", "传智播客"));
		//删除 所有 【慎用】
		IndexWriter.deleteAll();
		IndexWriter.commit();
		IndexWriter.close();
	}
	
	/**
	 * Lucene高级应用  高亮显示搜索
	 * @throws Exception
	 */
	@Test
	public void higligherSearch() throws Exception{
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] { "id", "content" },
				new IKAnalyzer());
		Query query = queryParser.parse("传智播客");
		
		//标签的前缀和后缀
		Formatter formatter =new SimpleHTMLFormatter("<span color='red'>", "</span>");
		QueryScorer queryScorer = new QueryScorer(query);
		//高亮显示对象
		Highlighter highlighter = new Highlighter(formatter, queryScorer);
		// 创建索引查询对象
		IndexSearcher indexSearcher = new IndexSearcher(
				DirectoryReader.open(FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory"))));
		// 排名前n的结果集
		TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
		// 得分文档集合
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc sd : scoreDocs) {
			int docId = sd.doc;
			Document document = indexSearcher.doc(docId);
			String content = document.get("content");
			//使用高亮显示器对结果集进行高亮转换
			String highlighterContent = highlighter.getBestFragment(new IKAnalyzer(), "content", content);
			// 文档的得分，即是文档的匹配度 得分越高排名就越靠前
			System.out.println(sd.score);
			System.out.println("搜索到的结果集id：" + document.get("id"));
			System.out.println("搜索到的结果集content：高亮后：" + highlighterContent);
		}
		// 关闭流
		indexSearcher.getIndexReader().close();
	}
	
	   //排序
		@Test
		public void sortIndex() throws Exception {
			DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory")));
			// 创建索引查询对象
			IndexSearcher indexSearcher = new IndexSearcher(reader);
			QueryParser queryParser=new QueryParser("title", new IKAnalyzer());
			Query query = queryParser.parse("faceBook");
			//SortField 指定排序的字段  指定字段的类型   指定字段的排序规则  false为升序（默认 ）   true为降序
			Sort sort=new Sort(new SortField("id", Type.LONG,true));
			// 排名前n的结果集 【结果集默认是升序】
			TopDocs topDocs = indexSearcher.search(query,10,sort);
			// 得分文档集合
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc sd : scoreDocs) {
				int docId = sd.doc;
				Document document = indexSearcher.doc(docId);
				// 文档的得分，即是文档的匹配度 得分越高排名就越靠前
				System.out.println(sd.score);
				System.out.println("搜索到的结果集id：" + document.get("id"));
				System.out.println("搜索到的结果集title：" + document.get("title"));
				System.out.println("搜索到的结果集content：" + document.get("content"));
			}
			// 关闭流
			indexSearcher.getIndexReader().close();
		}

	/**
	 * 分页查询    并根据 Id降序排列
	 * @param query
	 * @throws Exception
	 */
	@Test
	public void pageSortIndex() throws Exception {
		int pageNum=1;  //第几页
		int pageSize=2; //每页显示多少条   10
		//起始位置
		int start=(pageNum-1)*pageSize;
		//结束位置
		int end=pageNum*pageSize;
		DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("E:\\Eclipse_Code\\Lucene_Directory")));
		// 创建索引查询对象
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		QueryParser queryParser=new QueryParser("title", new IKAnalyzer());
		Query query = queryParser.parse("faceBook");
		//SortField 指定排序的字段  指定字段的类型   指定字段的排序规则  false为升序（默认 ）   true为降序
		Sort sort=new Sort(new SortField("id", Type.LONG,true));
		// 排名前n的结果集   
		TopDocs topDocs = indexSearcher.search(query,end,sort);
		// 得分文档集合
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (int i=start;i<scoreDocs.length;i++) {
			ScoreDoc sd= scoreDocs[i];
			int docId = sd.doc;
			Document document = indexSearcher.doc(docId);
			// 文档的得分，即是文档的匹配度 得分越高排名就越靠前
			System.out.println(sd.score);
			System.out.println("搜索到的结果集id：" + document.get("id"));
			System.out.println("搜索到的结果集title：" + document.get("title"));
			System.out.println("搜索到的结果集content：" + document.get("content"));
		}
		// 关闭流
		indexSearcher.getIndexReader().close();
	}

	

}
