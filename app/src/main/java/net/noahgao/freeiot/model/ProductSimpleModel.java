package net.noahgao.freeiot.model;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class ProductSimpleModel<T> extends Model {

    private String name;
    private String commit;
    private T owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public T getOwner() {
        return owner;
    }

    public void setOwner(T owner) {
        this.owner = owner;
    }
}
