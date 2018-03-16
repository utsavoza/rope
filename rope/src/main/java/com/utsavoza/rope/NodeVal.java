package com.utsavoza.rope;

public enum NodeVal {

  LEAF {
    private Object string;

    @Override NodeVal instance(Object val) {
      this.string = val;
      return this;
    }

    @Override Object get() {
      return this.string;
    }
  },

  INTERNAL {
    private Object listOfNodes;

    @Override NodeVal instance(Object val) {
      this.listOfNodes = val;
      return this;
    }

    @Override Object get() {
      return this.listOfNodes;
    }
  };

  abstract NodeVal instance(Object val);

  abstract Object get();
}
