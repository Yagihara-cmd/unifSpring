package jp.co.f1.spring.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import jp.co.f1.spring.Entity.Uniform;

@Repository
public class UniDao {

	// エンティティマネージャー
	private EntityManager entityManager;

	// クエリ生成用インスタンス
	private CriteriaBuilder builder;

	// クエリ実行用インスタンス
	private CriteriaQuery<Uniform> query;

	// 検索されるエンティティのルート
	private Root<Uniform> root;

	/*
	 * コンストラクタ（DB接続準備）
	 */
	public UniDao(EntityManager entityManager) {
		// EntityManager取得
		this.entityManager = entityManager;
		
		// クエリ生成用インスタンス
		builder = entityManager.getCriteriaBuilder();
		
		// クエリ実行用インスタンス
		query = builder.createQuery(Uniform.class);
		
		// 検索されるエンティティのルート
		root = query.from(Uniform.class);
	}
	
}
