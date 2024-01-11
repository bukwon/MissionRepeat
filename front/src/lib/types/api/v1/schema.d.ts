export interface paths {
    "/api/v1/posts": {
      /** 글 리스트 */
      get: operations["getItems"];
    };
    "/api/v1/posts/mine": {
      /** 내 글 리스트 */
      get: operations["getMine"];
    };
  }
  
  export type webhooks = Record<string, never>;
  
  export interface components {
    schemas: {
      GetItemsResponseBody: {
        items: components["schemas"]["PostDto"][];
      };
      PostDto: {
        /** Format: int64 */
        id: number;
        /** Format: date-time */
        createDate: string;
        /** Format: date-time */
        modifyDate: string;
        /** Format: int64 */
        authorId: number;
        authorUsername: string;
        title: string;
        body: string;
      };
      RsDataGetItemsResponseBody: {
        resultCode: string;
        /** Format: int32 */
        statusCode: number;
        msg: string;
        data: components["schemas"]["GetItemsResponseBody"];
        fail: boolean;
        success: boolean;
      };
      GetMineResponseBody: {
        items: components["schemas"]["PostDto"][];
      };
      RsDataGetMineResponseBody: {
        resultCode: string;
        /** Format: int32 */
        statusCode: number;
        msg: string;
        data: components["schemas"]["GetMineResponseBody"];
        fail: boolean;
        success: boolean;
      };
    };
    responses: never;
    parameters: never;
    requestBodies: never;
    headers: never;
    pathItems: never;
  }
  
  export type $defs = Record<string, never>;
  
  export type external = Record<string, never>;
  
  export interface operations {
  
    /** 글 리스트 */
    getItems: {
      responses: {
        /** @description OK */
        200: {
          content: {
            "application/json": components["schemas"]["RsDataGetItemsResponseBody"];
          };
        };
      };
    };
    /** 내 글 리스트 */
    getMine: {
      responses: {
        /** @description OK */
        200: {
          content: {
            "application/json": components["schemas"]["RsDataGetMineResponseBody"];
          };
        };
      };
    };
  }