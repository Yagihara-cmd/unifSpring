package jp.co.f1.spring.Dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jp.co.f1.spring.Entity.User;

@Repository
public class UserDao {
	
		// エンティティマネージャー
		private EntityManager entityManager;

		// クエリ生成用インスタンス
		private CriteriaBuilder builder;

		// クエリ実行用インスタンス
		private CriteriaQuery<User> query;

		// 検索されるエンティティのルート
		private Root<User> root;

		/**
		 * コンストラクタ（DB接続準備）
		 */
		public UserDao(EntityManager entityManager) {
			// EntityManager取得
			this.entityManager = entityManager;
			// クエリ生成用インスタンス
			builder = entityManager.getCriteriaBuilder();
			// クエリ実行用インスタンス
			query = builder.createQuery(User.class);
			// 検索されるエンティティのルート
			root = query.from(User.class);
		}

}