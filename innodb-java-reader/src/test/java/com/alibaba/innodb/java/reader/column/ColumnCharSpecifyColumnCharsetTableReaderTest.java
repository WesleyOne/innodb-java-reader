package com.alibaba.innodb.java.reader.column;

import com.alibaba.innodb.java.reader.AbstractTest;
import com.alibaba.innodb.java.reader.page.index.GenericRecord;
import com.alibaba.innodb.java.reader.schema.Column;
import com.alibaba.innodb.java.reader.schema.TableDef;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author xu.zx
 */
public class ColumnCharSpecifyColumnCharsetTableReaderTest extends AbstractTest {

  public TableDef getTableDef() {
    return new TableDef().setDefaultCharset("latin1")
        .addColumn(new Column().setName("id").setType("int(11)").setNullable(false).setPrimaryKey(true))
        .addColumn(new Column().setName("a").setType("varchar(64)").setNullable(true).setCharset("utf8"))
        .addColumn(new Column().setName("b").setType("varchar(1024)").setNullable(true).setCharset("utf8"))
        .addColumn(new Column().setName("c").setType("varchar(256)").setNullable(false).setCharset("gbk"))
        .addColumn(new Column().setName("d").setType("varchar(1024)").setNullable(false).setCharset("gbk"))
        .addColumn(new Column().setName("e").setType("varchar(512)").setNullable(true).setCharset("ujis"))
        .addColumn(new Column().setName("f").setType("varchar(1024)").setNullable(false).setCharset("ujis"));
  }

  @Test
  public void testCharsetForColumnMysql56() {
    assertTestOf(this)
        .withMysql56()
        .withTableDef(getTableDef())
        .checkAllRecordsIs(expected());
  }

  @Test
  public void testCharsetForColumnMysql57() {
    assertTestOf(this)
        .withMysql57()
        .withTableDef(getTableDef())
        .checkAllRecordsIs(expected());
  }

  @Test
  public void testCharsetForColumnMysql80() {
    assertTestOf(this)
        .withMysql80()
        .withTableDef(getTableDef())
        .checkAllRecordsIs(expected());
  }

  public Consumer<List<GenericRecord>> expected() {
    return recordList -> {

      assertThat(recordList.size(), is(2));

      GenericRecord r1 = recordList.get(0);
      assertThat(r1.get("id"), is(100));
      assertThat(r1.get("a"), is("维基百科（英语：Wikipedia，/ˌwɪkᵻˈpiːdiə/ 或"
          + " /ˌwɪkiˈpiːdiə/）是一个网络百科全书项目"));
      assertThat(r1.get("b"), is("吉米·威尔士与拉里·桑格两人的合作下于2001年1月13日在"
          + "互联网上推出的网站服务是维基百科的前身。\t1月15日，正式展开网络百科全书计划。桑格提出新"
          + "词“Wikipedia”\n创立之初，维基百科的目标是向全人类提供自由的百科全书。希望各地民众用自己选择"
          + "的语言参与编辑条目。书面印刷的百科全书多由专家主导编辑，再由出版商印刷、销售。\n维基百科号称"
          + "属于可自由访问和编辑的全球知识体，这意味维基百科除传统百科全书所收录的信息，也可以收录非学"
          + "术具有一定媒体关注度的动态事件。"));
      assertThat(r1.get("c"), is("2006年，杂志《时代》评选的时代年度风云人物“你”中，\t提到了"
          + "全球上百万人线上协作维基百科，促进维基百科成长。"));
      assertThat(r1.get("d"), is("维基百科是强调Copyleft自由内容、协同编辑以及多语版本一个的网络"
          + "百科全书项目，以互联网和Wiki技术作为媒介，已发展为一项世界性的百科全书协作计划。项目由非营利组织维"
          + "基媒体基金会负责相关的发展事宜。维基百科由全球各地的志愿者们合作编撰而成，整个计划已收录了超过3,000万"
          + "篇条目，其中英语维基百科以超过450万篇条目在数量上位居首位。\n\n维基百科允许访问网站的用户自由阅览和修"
          + "改绝大部分页面的内容，整个网站的总编辑次数已超过10亿次。截至2012年8月，整个维基百科计划总共有285种独"
          + "立运作的语言版本，且已被普遍认为是规模最大且最流行的网络百科全书。根据知名的Alexa Internet其网络流量"
          + "统计数字指出全世界总共有近3.65亿名民众使用维基百科，且维基百科也是全球浏览人数排名第五高的网站，同时也"
          + "是全世界最大的无广告网站。根据估计，维基百科每个月便有将近2.7亿的美国人民前往该网站浏览。"));
      assertThat(r1.get("e"), is("ルートヴィヒ・ヨーゼフ・ヨーハン・ウィトゲンシュタイン（独: Ludwig "
          + "Josef Johann Wittgenstein、1889年4月26日 - 1951年4月29日）は、オーストリアに生まれ主にイギリスで"
          + "活躍した哲学者である。後の言語哲学、分析哲学に強い影響を与えた。初期の著作である『論理哲学論考』に含ま"
          + "れる「語り得ぬものについては沈黙しなければならない」という命題は、一般にも有名な言葉の一つである。"));
      assertThat(r1.get("f"), is("イギリスにあるケンブリッジ大学・トリニティ・カレッジのバートランド・ラッ"
          + "セルのもとで哲学を学ぶが、第一次世界大戦後に発表された初期の著作『論理哲学論考』に哲学の完成をみて哲学の世"
          + "界から距離を置く。その後、小学校教師になるが、生徒を虐待したとされて辞職。トリニティ・カレッジに復学してふた"
          + "たび哲学の世界に身を置くこととなる。やがて、ケンブリッジ大学の教授にむかえられた彼は、『論考』での記号論理学中"
          + "心、言語間普遍論理想定の哲学に対する姿勢を変え、コミュニケーション行為に重点をずらしてみずからの哲学の再構築に"
          + "挑むが、結局、これは完成することはなく、癌によりこの世を去る。62才。生涯独身であった。なお、こうした再構築の試"
          + "みをうかがわせる文献として、遺稿となった『哲学探究』がよく挙げられる。そのため、ウィトゲンシュタインの哲学は、初"
          + "期と後期が分けられ、異なる視点から考察されることも多い。"));

      GenericRecord r2 = recordList.get(1);
      assertThat(r2.get("a"), is("a" + StringUtils.repeat("阿", 63)));
      // TODO mysql8.0 lob is not supported
      if (!isMysql8Flag.get()) {
        assertThat(r2.get("b"), is("b" + StringUtils.repeat("里", 1023)));
      }
      assertThat(r2.get("c"), is("c" + StringUtils.repeat("巴", 255)));
      // TODO mysql8.0 lob is not supported
      if (!isMysql8Flag.get()) {
        assertThat(r2.get("d"), is("d" + StringUtils.repeat("数", 1023)));
      }
      assertThat(r2.get("e"), is("e" + StringUtils.repeat("ン", 511)));
      // TODO mysql8.0 lob is not supported
      if (!isMysql8Flag.get()) {
        assertThat(r2.get("f"), is("f" + StringUtils.repeat("ト", 1023)));
      }
    };
  }

}
