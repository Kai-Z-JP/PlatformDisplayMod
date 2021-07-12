package jp.masa300.platformdisplaymod.block.tileentity;

import jp.masa300.platformdisplaymod.modelpack.modelset.ModelSetDisplay;
import jp.ngt.ngtlib.block.TileEntityPlaceable;
import jp.ngt.ngtlib.util.NGTUtil;
import jp.ngt.rtm.modelpack.IModelSelector;
import jp.ngt.rtm.modelpack.ModelPackManager;
import jp.ngt.rtm.modelpack.ScriptExecuter;
import jp.ngt.rtm.modelpack.state.ResourceState;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityPlatformDisplay extends TileEntityPlaceable implements IModelSelector {
    private ResourceState state = new ResourceState(this);
    private String modelName = "";
    private Block renderBlock;
    private ScriptExecuter executer = new ScriptExecuter();

    private ModelSetDisplay myModelSet;
    public int tick;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        String s = nbt.getString("ModelName");
        if(s == null || s.length() == 0) {
            s = this.getDefaultName();
        }
        this.setModelName(s);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("modelName", this.modelName);
    }

    private void sendPacket() {
        NGTUtil.sendPacketToClient(this);
    }

    @Override
    public ResourceState getResourceState() {
        return this.state;
    }

    @Override
    public String getModelType() {
        return "ModelDisplay";
    }

    @Override
    public String getModelName() {
        return this.modelName;
    }

    @Override
    public void setModelName(String par1) {
        this.modelName = par1;
        this.myModelSet = null;
        if (this.worldObj == null || !this.worldObj.isRemote) {
            this.markDirty();
            this.sendPacket();
        }
    }

    @Override
    public int[] getPos() {
        return new int[]{this.xCoord, this.yCoord, this.zCoord};
    }

    @Override
    public boolean closeGui(String par1) {
        return true;
    }

    @Override
    public ModelSetDisplay getModelSet() {
        if (this.myModelSet == null || this.myModelSet.isDummy()) {
            this.myModelSet = ModelPackManager.INSTANCE.getModelSet("ModelDisplay", this.modelName);
        }
        return this.myModelSet;
    }

    /**NBTにモデル名が含まれない場合に使用*/
    protected String getDefaultName() {
        return "PlatformDisplay01";
    }
}