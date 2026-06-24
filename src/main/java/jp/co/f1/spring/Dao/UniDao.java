package jp.co.f1.spring.Dao;

import java.util.ArrayList;

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

	public ArrayList<Uniform> find(String uniid, String uniname, String price , String stock) {

		//SELECT1句設定
		query.select(root);

		//WHERE句設定
		query.where(
				builder.like(root.get("uniid"), "%" + uniid + "%"),
				builder.like(root.get("uniname"), "%" + uniname + "%"),
				builder.like(root.get("uniname"), "%" + price + "%"),
				builder.like(root.get("price"), "%" + stock + "%"));

		//クエリ実行
		return (ArrayList<Uniform>) entityManager.createQuery(query).getResultList();
	}

}
