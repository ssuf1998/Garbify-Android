package indi.ssuf1998.garbify.db.def;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.UserDao;
import indi.ssuf1998.garbify.db.HistoryDao;

@Entity
public class History {
  @Id(autoincrement = true)
  private Long id;

  private String pic;
  private String readablePredictsJson;
  private long createTime;

  private String userId;

  @ToOne(joinProperty = "userId")
  private User user;

  /** Used to resolve relations */
  @Generated(hash = 2040040024)
  private transient DaoSession daoSession;

  /** Used for active entity operations. */
  @Generated(hash = 1462128466)
  private transient HistoryDao myDao;

  @Generated(hash = 1904963732)
  public History(Long id, String pic, String readablePredictsJson,
          long createTime, String userId) {
      this.id = id;
      this.pic = pic;
      this.readablePredictsJson = readablePredictsJson;
      this.createTime = createTime;
      this.userId = userId;
  }

  @Generated(hash = 869423138)
  public History() {
  }

  public Long getId() {
      return this.id;
  }

  public void setId(Long id) {
      this.id = id;
  }

  public String getPic() {
      return this.pic;
  }

  public void setPic(String pic) {
      this.pic = pic;
  }

  public String getReadablePredictsJson() {
      return this.readablePredictsJson;
  }

  public void setReadablePredictsJson(String readablePredictsJson) {
      this.readablePredictsJson = readablePredictsJson;
  }

  public long getCreateTime() {
      return this.createTime;
  }

  public void setCreateTime(long createTime) {
      this.createTime = createTime;
  }

  public String getUserId() {
      return this.userId;
  }

  public void setUserId(String userId) {
      this.userId = userId;
  }

  @Generated(hash = 1867105156)
  private transient String user__resolvedKey;

  /** To-one relationship, resolved on first access. */
  @Generated(hash = 538271798)
  public User getUser() {
      String __key = this.userId;
      if (user__resolvedKey == null || user__resolvedKey != __key) {
          final DaoSession daoSession = this.daoSession;
          if (daoSession == null) {
              throw new DaoException("Entity is detached from DAO context");
          }
          UserDao targetDao = daoSession.getUserDao();
          User userNew = targetDao.load(__key);
          synchronized (this) {
              user = userNew;
              user__resolvedKey = __key;
          }
      }
      return user;
  }

  /** called by internal mechanisms, do not call yourself. */
  @Generated(hash = 1065606912)
  public void setUser(User user) {
      synchronized (this) {
          this.user = user;
          userId = user == null ? null : user.getId();
          user__resolvedKey = userId;
      }
  }

  /**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 128553479)
  public void delete() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.delete(this);
  }

  /**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 1942392019)
  public void refresh() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.refresh(this);
  }

  /**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 713229351)
  public void update() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.update(this);
  }

  /** called by internal mechanisms, do not call yourself. */
  @Generated(hash = 851899508)
  public void __setDaoSession(DaoSession daoSession) {
      this.daoSession = daoSession;
      myDao = daoSession != null ? daoSession.getHistoryDao() : null;
  }

}
