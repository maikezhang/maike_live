package cn.idongjia.live.support;

import cn.idongjia.common.pagination.Pagination;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * 类PaginationInterceptor.java的实现描述
 *
 * @author dongjia_lj 2015年7月6日 上午10:30:25
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection
        .class})})
public class PaginationInterceptor implements Interceptor {

    private static final Log logger = LogFactory.getLog(PaginationInterceptor.class);
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new
            DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private static String defaultDialect = "mysql";                                       //
    // 数据库类型(默认为mysql)
    private static String defaultPageSqlId = ".*Page$";                                     //
    // 需要拦截的ID(正则匹配)
    private static String dialect = "";                                            // 数据库类型
    // (默认为mysql)
    private static String pageSqlId = "";                                            // 需要拦截的ID
    // (正则匹配)

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,
                DEFAULT_OBJECT_FACTORY,
                DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
                    DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
                    DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate" +
                ".configuration");
        dialect = configuration.getVariables().getProperty("dialect");
        if (null == dialect || "".equals(dialect)) {
            logger.warn("Property dialect is not setted,use default 'mysql' ");
            dialect = defaultDialect;
        }
        pageSqlId = configuration.getVariables().getProperty("pageSqlId");
        if (null == pageSqlId || "".equals(pageSqlId)) {
            logger.warn("Property pageSqlId is not setted,use default '.*Page$' ");
            pageSqlId = defaultPageSqlId;
        }
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue
                ("delegate.mappedStatement");
        // 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
        if (mappedStatement.getId().matches(pageSqlId)) {
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject == null) {
                throw new NullPointerException("parameterObject is null!");
            } else {
                Integer page = (Integer) metaStatementHandler.getValue("delegate.boundSql" +
                        ".parameterObject.page");
                page = null != page ? page : 1;
                Integer limit = (Integer) metaStatementHandler.getValue("delegate.boundSql" +
                        ".parameterObject.limit");
                limit = null != limit ? limit : 10;
                Pagination pagination = (Pagination) metaStatementHandler.getValue("delegate" +
                        ".boundSql.parameterObject.pagination");
                Boolean needCount = (Boolean) metaStatementHandler.getValue("delegate" +
                        ".boundSql.parameterObject.needCount");
                String sql = boundSql.getSql();
                // 重写sql
                String pageSql = buildPageSql(sql, page, limit);
                metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
                // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
                metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
                metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
                Connection connection = (Connection) invocation.getArgs()[0];
                // 重设分页参数里的总页数等
                pagination = null != pagination ? pagination : new Pagination();
                if (null != needCount && needCount) {
                    setPageParameter(sql, connection, mappedStatement, boundSql, pagination, limit);
                    metaStatementHandler.setValue("delegate.boundSql.parameterObject.pagination",
                            pagination);
                }
            }
        }
        // 将执行权交给下一个拦截器
        return invocation.proceed();
    }

    /**
     * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用者就可用通过 分页参数
     * <code>PageParameter</code>获得相关信息。
     *
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     */
    private void setPageParameter(String sql, Connection connection, MappedStatement
            mappedStatement,BoundSql boundSql, Pagination pagination, Integer pageSize) {
        // 记录总记录数
        String countSql = "";
        if (mappedStatement.getId().endsWith("queryReviewByPage")) {
            countSql = sql.substring(sql.indexOf("from"), sql.indexOf("order by"));
            countSql = "select count(*) total " + countSql;
        } else {
            countSql = "select count(*) total from (" + sql + ") t";
        }
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());
            // 设置复杂参数,eg:list
            List<ParameterMapping> parameters = countBS.getParameterMappings();
            parameters.forEach(p -> {
                String property = p.getProperty();
                if (boundSql.hasAdditionalParameter(property)) {
                    countBS.setAdditionalParameter(property, boundSql.getAdditionalParameter(property));
                }
            });

            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            pagination.setCount(totalCount);
            int totalPage = totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1);
            totalPage = totalPage == 0 ? 1 : totalPage;
            pagination.setPageCount(totalPage);

        } catch (SQLException e) {
            logger.error("Ignore this exception", e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
            try {
                countStmt.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
        }

    }

    /**
     * 对SQL参数(?)设值
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql
            boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
                parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

    /**
     * 根据数据库类型，生成特定的分页sql
     *
     * @param sql
     * @return
     */
    private String buildPageSql(String sql, Integer curPage, Integer pagesize) {
        if (null != curPage && null != pagesize) {
            StringBuilder pageSql = new StringBuilder();
            if ("mysql".equals(dialect)) {
                pageSql = buildPageSqlForMysql(sql, curPage, pagesize);
            } else if ("oracle".equals(dialect)) {
                pageSql = buildPageSqlForOracle(sql, curPage, pagesize);
            } else if ("db2".equals(dialect)) {
                pageSql = buildPageSqlForDB2(sql, curPage, pagesize);
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    /**
     * *DB2的分页语句
     *
     * @param sql
     * @return
     */
    public StringBuilder buildPageSqlForDB2(String sql, int curPage, int pagesize) {
        StringBuilder pageSql = new StringBuilder(100);
        int beginrow = (curPage - 1) * pagesize + 1;
        pageSql.append("select * from (select rspasfasd.*,rownumber() over(ORDER BY '' ASC) as " +
                "asgdsadfgsaf from (");
        pageSql.append(sql).append(") as rspasfasd ) as rpasdasdasd where rpasdasdasd" +
                ".asgdsadfgsaf BETWEEN ");
        pageSql.append(beginrow).append(" AND ").append(beginrow + pagesize - 1);

        return pageSql;
    }

    /**
     * mysql的分页语句
     *
     * @param sql
     * @return String
     */
    public StringBuilder buildPageSqlForMysql(String sql, int curPage, int pagesize) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginrow = String.valueOf((curPage - 1) * pagesize);
        pageSql.append(sql);
        pageSql.append(" limit " + beginrow + "," + pagesize);
        return pageSql;
    }

    /**
     * 参考hibernate的实现完成oracle的分页
     *
     * @param sql
     * @return String
     */
    public StringBuilder buildPageSqlForOracle(String sql, int curPage, int pagesize) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginrow = String.valueOf((curPage - 1) * pagesize);
        String endrow = String.valueOf(curPage * pagesize);

        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp where rownum <= ").append(endrow);
        pageSql.append(") where row_id > ").append(beginrow);
        return pageSql;
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
