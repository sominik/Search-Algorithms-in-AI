public class Loot extends BaseEntity{
    public int money;
    public int food;
    public boolean used;
    public boolean useMoney;
    public boolean useFood;
    public Loot(int money,int food){
        super('L');
        this.money = money;
        this.food = food;
        this.used = false;
        this.useFood = false;
        this.useMoney = false;
    }
    public void useMoney(Player player){
        player.changeMoney(this.money);
        this.used = true;
        this.useMoney=true;
    }
    public void useFood(Player player){
        player.changeFood(this.food);
        this.used = true;
        this.useFood=true;
    }
    @Override
    public Loot copy(){
        Loot loot = new Loot(this.money,this.food);
        loot.used = this.used;
        return loot;
    }
}
