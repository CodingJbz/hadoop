/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.io.erasurecode.coder;

import org.apache.hadoop.io.erasurecode.CodecUtil;
import org.apache.hadoop.io.erasurecode.ECBlock;
import org.apache.hadoop.io.erasurecode.ECBlockGroup;
import org.apache.hadoop.io.erasurecode.ECSchema;
import org.apache.hadoop.io.erasurecode.rawcoder.RawErasureDecoder;

/**
 * Reed-Solomon erasure decoder that decodes a block group.
 *
 * It implements {@link ErasureCoder}.
 */
public class RSErasureDecoder extends AbstractErasureDecoder {
  private RawErasureDecoder rsRawDecoder;

  public RSErasureDecoder(int numDataUnits, int numParityUnits) {
    super(numDataUnits, numParityUnits);
  }

  public RSErasureDecoder(ECSchema schema) {
    super(schema);
  }

  @Override
  protected ErasureCodingStep prepareDecodingStep(final ECBlockGroup blockGroup) {

    ECBlock[] inputBlocks = getInputBlocks(blockGroup);
    ECBlock[] outputBlocks = getOutputBlocks(blockGroup);

    RawErasureDecoder rawDecoder = checkCreateRSRawDecoder();
    return new ErasureDecodingStep(inputBlocks,
        getErasedIndexes(inputBlocks), outputBlocks, rawDecoder);
  }

  private RawErasureDecoder checkCreateRSRawDecoder() {
    if (rsRawDecoder == null) {
      rsRawDecoder = CodecUtil.createRSRawDecoder(getConf(),
          getNumDataUnits(), getNumParityUnits());
    }
    return rsRawDecoder;
  }

  @Override
  public void release() {
    if (rsRawDecoder != null) {
      rsRawDecoder.release();
    }
  }
}
