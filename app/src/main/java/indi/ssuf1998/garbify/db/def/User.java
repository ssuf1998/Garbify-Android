package indi.ssuf1998.garbify.db.def;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import indi.ssuf1998.garbify.db.DaoSession;
import indi.ssuf1998.garbify.db.HistoryDao;
import indi.ssuf1998.garbify.db.UserDao;

@Entity
public class User {
  @Id
  private String id;
  private String password;
  private String avatar;
  private String nickname;
  private int exp;

  private String token;

  @ToMany(referencedJoinProperty = "userId")
  private List<History> histories;
  /** Used to resolve relations */
  @Generated(hash = 2040040024)
  private transient DaoSession daoSession;
  /** Used for active entity operations. */
  @Generated(hash = 1507654846)
  private transient UserDao myDao;

  @Generated(hash = 1069998578)
public User(String id, String password, String avatar, String nickname, int exp,
        String token) {
    this.id = id;
    this.password = password;
    this.avatar = avatar;
    this.nickname = nickname;
    this.exp = exp;
    this.token = token;
}

@Generated(hash = 586692638)
  public User() {
  }

  public String getId() {
      return this.id;
  }

  public void setId(String id) {
      this.id = id;
  }

  public String getPassword() {
      return this.password;
  }

  public void setPassword(String password) {
      this.password = password;
  }

  public String getAvatar() {
      return this.avatar;
  }

  public void setAvatar(String avatar) {
      this.avatar = avatar;
  }

  public String getNickname() {
      return this.nickname;
  }

  public void setNickname(String nickname) {
      this.nickname = nickname;
  }

  public int getExp() {
      return this.exp;
  }

  public void setExp(int exp) {
      this.exp = exp;
  }

  public String getToken() {
      return this.token;
  }

  public void setToken(String token) {
      this.token = token;
  }

  /**
   * To-many relationship, resolved on first access (and after reset).
   * Changes to to-many relations are not persisted, make changes to the target entity.
   */
  @Generated(hash = 682720167)
  public List<History> getHistories() {
      if (histories == null) {
          final DaoSession daoSession = this.daoSession;
          if (daoSession == null) {
              throw new DaoException("Entity is detached from DAO context");
          }
          HistoryDao targetDao = daoSession.getHistoryDao();
          List<History> historiesNew = targetDao._queryUser_Histories(id);
          synchronized (this) {
              if (histories == null) {
                  histories = historiesNew;
              }
          }
      }
      return histories;
  }

  /** Resets a to-many relationship, making the next get call to query for a fresh result. */
  @Generated(hash = 36099026)
  public synchronized void resetHistories() {
      histories = null;
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
  @Generated(hash = 2059241980)
  public void __setDaoSession(DaoSession daoSession) {
      this.daoSession = daoSession;
      myDao = daoSession != null ? daoSession.getUserDao() : null;
  }

}
